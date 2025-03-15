import tkinter as tk
from tkinter import messagebox, font
from model.game import Move, CaroGame, Player
from model.network import NetworkManager
from view.host_view import HostView
from view.join_view import JoinView
import socket

class OnlineCaroController:
    def __init__(self, game, board, menu):
        self._game = game
        self._board = board
        self._menu = menu
        self._network = None
        self._is_host = False
        self._my_label = None
        self._opponent_label = None
        self._scores = {"X": 0, "O": 0}
        self._board.update_score(self._scores)
        self._notification = None

    def handle_move(self, row, col):
        if self._game.current_player.label != self._my_label:
            messagebox.showinfo("Wait", "Not your turn!", parent=self._board)
            return
        move = Move(row, col, self._my_label)
        if not self._game.is_valid_move(move):
            messagebox.showinfo("Invalid Move", "Position taken or game over!", parent=self._board)
            return
        self._game.process_move(move)
        self._board.update_button(row, col, self._my_label)
        self._network.send_move(row, col)
        self._check_game_state()
        self._game.toggle_player()
        self._board.update_display(f"Opponent's turn ({self._opponent_label})")

    def _check_game_state(self):
        if self._game.has_winner():
            self._board.highlight_cells(self._game.winner_combo)
            winner = self._game.current_player.label
            self._scores[winner] += 1
            self._board.update_display(f"{winner} won!", "yellow")
            self._board.update_score(self._scores)
        elif self._game.is_tied():
            self._board.update_display("Tied game!", "red")
            self._board.update_score(self._scores)

    def set_online_mode(self, is_host=True, host_ip=None):
        self._is_host = is_host
        self._network = NetworkManager()
        if is_host:
            local_ip = self._get_local_ip()
            self._notification = HostView(self._board, local_ip, self._copy_ip, self._close_notification)
            self._board.update_display("Waiting for opponent to connect...")
            self._network.host(callback=self._on_host_connected)
        else:
            self._notification = JoinView(self._board, self._try_connect, self._cancel_join)
            self._board.update_display("Enter IP to join...")

    def _on_host_connected(self):
        if self._notification:
            self._notification.update_status("Connected!", "green")
            self._notification.after(1000, self._notification.destroy)
            self._notification = None
        self._my_label = "X"
        self._opponent_label = "O"
        self._game.current_player = Player(label="X", color="")
        self._board.update_display("Your turn (X)")
        self._board.after(100, self._check_opponent_move)

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
        try:
            self._network.join(host_ip)
            self._notification.update_status("Connected!", "green")
            self._notification.after(1000, self._notification.destroy)
            self._notification = None
            self._my_label = "O"
            self._opponent_label = "X"
            self._game.current_player = Player(label="X", color="")
            self._board.update_display("Opponent's turn (X)")
            self._board.after(100, self._check_opponent_move)
        except Exception as e:
            self._notification.update_status(f"Failed: {str(e)}", "red")

    def _cancel_join(self):
        if self._network:
            self._network.close()
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

    def _check_opponent_move(self):
        if self._network:
            move = self._network.get_move()
            if move:
                row, col = move
                move_obj = Move(row, col, self._opponent_label)
                self._game.process_move(move_obj)
                self._board.update_button(row, col, self._opponent_label)
                self._check_game_state()
                if not self._game.has_winner() and not self._game.is_tied():
                    self._game.toggle_player()
                    self._board.update_display(f"Your turn ({self._my_label})")
        self._board.after(100, self._check_opponent_move)

    def reset_game(self):
        self._game.reset_game()
        self._board.reset_board()
        self._board.update_display(f"Your turn ({self._my_label})" if self._is_host else f"Opponent's turn ({self._opponent_label})")
        self._board.update_score(self._scores)

    def back_to_menu(self):
        if self._network:
            self._network.close()
        self._board.withdraw()
        self._menu.deiconify()

    def start(self):
        pass

    def suggest_move(self):
        messagebox.showinfo("Invalid", "Suggest move not available in online mode!", parent=self._board)