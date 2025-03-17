# model/network.py
import socket
import json
import threading

class NetworkManager:
    def __init__(self):
        self.server_socket = None
        self.client_socket = None
        self.running = False
        self.connection = None

    def host(self, port, on_connected):
        self.server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        self.server_socket.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
        self.server_socket.bind(('0.0.0.0', port))
        self.server_socket.listen(1)
        print(f"Waiting for a connection...")
        self.connection, addr = self.server_socket.accept()
        print(f"Connected to {addr}")
        on_connected()

    def join(self, host_ip, port):
        self.client_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        self.client_socket.connect((host_ip, port))

    def send_message(self, message):
        if self.connection:
            self.connection.sendall(message.encode())
        elif self.client_socket:
            self.client_socket.sendall(message.encode())

    def receive_message(self, timeout=None):
        if timeout is not None:
            if self.connection:
                self.connection.settimeout(timeout)
            elif self.client_socket:
                self.client_socket.settimeout(timeout)

        try:
            if self.connection:
                data = self.connection.recv(1024).decode()
            elif self.client_socket:
                data = self.client_socket.recv(1024).decode()
            else:
                return None

            if not data:
                return None
            return json.loads(data)
        except (socket.timeout, json.JSONDecodeError):
            return None
        finally:
            if timeout is not None:
                if self.connection:
                    self.connection.settimeout(None)
                elif self.client_socket:
                    self.client_socket.settimeout(None)

    def close(self):
        self.running = False
        if self.connection:
            self.connection.close()
        if self.client_socket:
            self.client_socket.close()
        if self.server_socket:
            self.server_socket.close()