# controller/online_controller.py
import tkinter as tk
from tkinter import messagebox, font
from model.game import Move
from model.network_service import NetworkService
from model.online_game_manager import OnlineGameManager
from controller.online_rematch_handler import OnlineRematchHandler
from view.host_view import HostView
from view.join_view import JoinView
import socket
import threading
from controller.online_ai_controller import OnlineAIController

class OnlineCaroController:
    def __init__(self, game, board, menu):
        print("Initializing OnlineCaroController...")
        self._board = board
        self._menu = menu
        self._network_service = None
        self._is_host = False
        self._my_label = None
        self._opponent_label = None
        self._notification = None
        self._rematch_button = None

        self.game_manager = OnlineGameManager(game, board, is_host=self._is_host)
        self.rematch_handler = OnlineRematchHandler(self, self.game_manager)
        self._board.protocol("WM_DELETE_WINDOW", self.handle_window_close)

        self.online_ai_controller = OnlineAIController(self._network_service, self.game_manager, self._board)

    def handle_move(self, row, col):
        if self.game_manager.current_player_label != self._my_label:
            messagebox.showinfo("Wait", "Not your turn!", parent=self._board)
            return

        if self.game_manager.game.has_winner() or self.game_manager.game.is_tied():
            messagebox.showinfo("Game Over", "The game is already over!", parent=self._board)
            return

        if self.game_manager.handle_move(row, col, self._my_label):
            self._network_service.send_message("move", row=row, col=col, player=self._my_label, next_player=self.game_manager.current_player_label)

    def set_online_mode(self, is_host=True, host_ip=None):
        print(f"Setting online mode, is_host={is_host}, host_ip={host_ip}")
        self._is_host = is_host
        self.game_manager.is_host = is_host
        self._network_service = NetworkService(is_host, self)
        self.online_ai_controller.network_service = self._network_service

        if is_host:
            local_ip = self._get_local_ip()
            self._notification = HostView(self._board, local_ip, self._copy_ip, self._close_notification)
            self._board.update_display("Waiting for opponent to connect...")
            threading.Thread(target=self._network_service.host, daemon=True).start()
        else:
            self._notification = JoinView(self._board, self._try_connect, self._cancel_join)
            self._board.update_display("Enter IP to join...")

    def connection_established(self):
        print("Connection established!")
        if self._notification:
            self._notification.update_status("Connected!", "green")
            self._notification.after(1000, self._notification.destroy)
            self._notification = None
        if self._is_host:
            self._my_label = "X"
            self._opponent_label = "O"
            self.game_manager.set_players(self._my_label, self._opponent_label)
            self._network_service.send_message("start_game", my_label="O", opponent_label="X")
            self._board.update_display("Your turn (X)")
        else:
            self._board.update_display("Waiting for game to start...")

    def connection_failed(self, error_message):
        print(f"Connection failed: {error_message}")
        if self._notification:
            self._notification.update_status(f"Failed: {error_message}", "red")

    def _copy_ip(self):
        ip = self._get_local_ip()
        self._board.clipboard_append(ip)
        if self._notification:
            self._notification.update_status("IP copied!", "green")
            self._notification.after(1000, lambda: self._notification.update_status("Share IP with your friend", "#3C2F2F"))

    def _close_notification(self):
        if self._notification:
            self._notification.destroy()
            self._notification = None

    def _try_connect(self, host_ip):
        if not host_ip:
            self._notification.update_status("Please enter an IP!", "red")
            return
        self._notification.update_status("Connecting...", "cyan")
        self._network_service.join(host_ip)

    def _cancel_join(self):
        if self._network_service:
            self._network_service.close()
        if self._notification:
            self._notification.destroy()
            self._notification = None
        self.back_to_menu()

    def _get_local_ip(self):
        s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
        try:
            s.connect(('10.255.255.255', 1))
            ip = s.getsockname()[0]
        except Exception:
            ip = '127.0.0.1'
        finally:
            s.close()
        return ip

    def opponent_moved(self, row, col, player):
        self.game_manager.process_opponent_move(row, col, player)

    def opponent_disconnected(self):
        messagebox.showinfo("Disconnected", "Opponent has disconnected.", parent=self._board)
        self.show_back_to_menu_button()

    def show_back_to_menu_button(self):
        back_to_menu_btn = tk.Button(self._board, text="Back to Menu",
                                     font=font.Font(family="Arial", size=12),
                                     bg="#ff9999", fg="#000000",
                                     activebackground="#ff6666",
                                     borderwidth=1, relief="flat",
                                     command=self.back_to_menu)
        back_to_menu_btn.place(relx=0.5, rely=0.8, anchor="center")

    def show_rematch_button(self):
        if self._rematch_button:
            self._rematch_button.destroy()
        rematch_btn = tk.Button(self._board, text="Request Rematch",
                                font=font.Font(family="Arial", size=12),
                                bg="#aaddff", fg="#000000",
                                activebackground="#88ccff",
                                borderwidth=1, relief="flat",
                                command=self.request_rematch)
        rematch_btn.place(relx=0.5, rely=0.7, anchor="center")
        self._rematch_button = rematch_btn

    def request_rematch(self):
        self.rematch_handler.request_rematch()

    def handle_rematch_request(self):
        self.rematch_handler.handle_rematch_request()

    def handle_rematch_accepted(self):
        self.rematch_handler.handle_rematch_accepted()

    def reset_game(self):
        print("Reset game called...")
        self.request_rematch()

    def back_to_menu(self):
        if self._network_service:
            self._network_service.close()
        if self._rematch_button:
            self._rematch_button.destroy()
            self._rematch_button = None
        self._board.withdraw()
        self._menu.deiconify()

    def start(self):
        pass

    def suggest_move(self):
        self.online_ai_controller.request_suggestion()

    def send_message(self, message_type, **kwargs):
        self._network_service.send_message(message_type, **kwargs)

    def get_my_label(self):
        return self._my_label

    def send_chat_message(self, message):
        if self._my_label:
            self._network_service.send_message("chat", player=self._my_label, message=message)
            self.display_chat_message(self._my_label, message)

    def display_chat_message(self, player, message):
        formatted_message = f"{player}: {message}\n"
        self._board.chat_display.configure(state='normal')
        self._board.chat_display.insert(tk.END, formatted_message)
        self._board.chat_display.see(tk.END)
        self._board.chat_display.configure(state='disabled')

    def handle_window_close(self):
        if self._network_service:
            self._network_service.close()
        self.back_to_menu()

    def start_game(self, my_label, opponent_label):
        if not self._is_host:
            self._my_label = my_label
            self._opponent_label = opponent_label
            self.game_manager.set_players(self._my_label, self._opponent_label)
            self.game_manager.update_turn("X")
            self._board.reset_board()
            self._board.update_display("Opponent's turn (X)")

    def update_score(self, scores):
        self.game_manager.update_score_from_host(scores)

    def handle_game_over(self, winner, scores):
        print(f"Game over received: winner={winner}, scores={scores}")
        self.game_manager.scores = scores
        self.game_manager._game_ended = True
        self._board.update_score(scores)
        if winner:
            self._board.highlight_cells(self.game_manager.game.winner_combo)
            self._board.update_display(f"{winner} won!", "yellow")
        else:
            self._board.update_display("Tied game!", "red")