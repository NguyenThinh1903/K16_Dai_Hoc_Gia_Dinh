# model/network_service.py
import socket
import threading
import json
import time
from model.network import NetworkManager

class NetworkService:
    def __init__(self, is_host, controller):
        self.is_host = is_host
        self.controller = controller
        self.network_manager = NetworkManager()
        self.connected = False
        self.opponent_disconnected = False
        self.message_handlers = {
            "move": self.handle_move,
            "chat": self.handle_chat,
            "request_rematch": self.handle_request_rematch,
            "rematch_accepted": self.handle_rematch_accepted,
            "disconnect": self.handle_disconnect,
            "start_game": self.handle_start_game,
            "update_score": self.handle_update_score,
            "game_over": self.handle_game_over,  # Thêm handler cho game_over
        }
        self.send_queue = []

    def host(self, port=5555):
        self.network_manager.host(port, self.on_connected)

    def join(self, host_ip, port=5555):
        try:
            self.network_manager.join(host_ip, port)
            self.on_connected()
        except Exception as e:
            self.controller.connection_failed(str(e))

    def on_connected(self):
        self.connected = True
        self.network_manager.running = True
        threading.Thread(target=self.listen, daemon=True).start()
        self.controller.connection_established()
        self.process_send_queue()

    def listen(self):
        while self.network_manager.running:
            try:
                message = self.network_manager.receive_message(timeout=1.0)
                if message:
                    self.process_message(message)
                else:
                    time.sleep(0.1)
            except socket.error as e:
                print(f"Socket error: {e}")
                if self.connected and not self.opponent_disconnected:
                    if self.is_connection_lost():
                        self.handle_disconnect()
                        break
            except Exception as e:
                print(f"Unexpected error in listen: {e}")
                time.sleep(1)

    def is_connection_lost(self):
        try:
            if self.network_manager.connection:
                self.network_manager.connection.send(b"")
            elif self.network_manager.client_socket:
                self.network_manager.client_socket.send(b"")
            return False
        except socket.error:
            return True

    def send_message(self, message_type, **kwargs):
        message = {"type": message_type, **kwargs}
        if self.connected:
            try:
                self.network_manager.send_message(json.dumps(message))
                print(f"Sent message: {message_type}")
            except socket.error as e:
                print(f"Failed to send message: {e}")
                if self.connected:
                    self.handle_disconnect()
            except Exception as e:
                print(f"Unexpected error when sending message: {e}")
        else:
            self.send_queue.append(message)

    def process_send_queue(self):
        for message in self.send_queue:
            try:
                self.network_manager.send_message(json.dumps(message))
            except socket.error as e:
                print(f"Failed to send queued message: {e}")
        self.send_queue = []

    def process_message(self, message):
        print(f"Received message: {message}")
        handler = self.message_handlers.get(message["type"])
        if handler:
            handler(message)
        else:
            print(f"Unknown message type: {message['type']}")

    def handle_move(self, message):
        self.controller.opponent_moved(message["row"], message["col"], message["player"])

    def handle_chat(self, message):
        self.controller.display_chat_message(message['player'], message["message"])

    def handle_request_rematch(self, message):
        self.controller.handle_rematch_request()

    def handle_rematch_accepted(self, message):
        self.controller.handle_rematch_accepted()

    def handle_disconnect(self, message=None):
        if not self.opponent_disconnected:
            self.opponent_disconnected = True
            self.connected = False
            self.network_manager.close()
            self.controller.opponent_disconnected()
            print("Disconnected from opponent")

    def handle_start_game(self, message):
        if not self.is_host:
            self.controller.start_game(message["my_label"], message["opponent_label"])
            print("Client received start_game")

    def handle_update_score(self, message):
        if not self.is_host:
            self.controller.update_score(message["scores"])

    def handle_game_over(self, message):
        winner = message.get("winner")  # None nếu hòa
        scores = message["scores"]
        self.controller.handle_game_over(winner, scores)

    def close(self):
        if self.connected:
            self.send_message("disconnect")
        self.connected = False
        self.network_manager.close()