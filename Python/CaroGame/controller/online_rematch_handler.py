import tkinter as tk
from tkinter import messagebox, font
import time

class OnlineRematchHandler:
    def __init__(self, controller, game_manager):
        self.controller = controller
        self.game_manager = game_manager
        self.rematch_requested = False

    def request_rematch(self):
        if self.game_manager.game.has_winner() or self.game_manager.game.is_tied():
            if self.rematch_requested:
                messagebox.showinfo("Rematch", "Waiting for opponent...", parent=self.controller._board)
            else:
                self.rematch_requested = True
                self.controller.send_message("request_rematch")
                messagebox.showinfo("Rematch", "Rematch request sent!", parent=self.controller._board)
                if self.game_manager._check_game_state() == "game_over":
                    self.controller.show_rematch_button()
        else:
            messagebox.showinfo("Rematch", "Waiting for opponent...", parent=self.controller._board)

    def handle_rematch_request(self):
        if self.rematch_requested:
            self.start_new_game()
        else:
            if messagebox.askyesno("Rematch", "Opponent requests a rematch. Accept?", parent=self.controller._board):
                self.controller.send_message("rematch_accepted")
                self.start_new_game()
            else:
                self.rematch_requested = False

    def handle_rematch_accepted(self):
        self.start_new_game()

    def start_new_game(self):
        print("Starting new game...")  # Debug
        if self.controller._is_host:
            new_my_label = "X"
            new_opponent_label = "O"
        else:
            new_my_label = "O"
            new_opponent_label = "X"

        self.game_manager.set_players(new_my_label, new_opponent_label)
        self.controller._my_label = new_my_label
        self.controller._opponent_label = new_opponent_label

        self.game_manager.reset_game()
        self.rematch_requested = False

        if hasattr(self.controller, '_rematch_button') and self.controller._rematch_button:
            self.controller._rematch_button.destroy()
            self.controller._rematch_button = None

        if self.controller._is_host:
            self.controller.game_manager.update_turn("X")
            self.controller._board.update_display("Your turn (X)")
        else:
            self.controller.game_manager.update_turn("X")
            self.controller._board.update_display("Opponent's turn (X)")

        if self.controller._is_host:
            max_retries = 3
            for attempt in range(max_retries):
                try:
                    self.controller.send_message("start_game", my_label=new_opponent_label, opponent_label=new_my_label)
                    print(f"Host sent start_game message (attempt {attempt + 1})")
                    break
                except Exception as e:
                    print(f"Failed to send start_game (attempt {attempt + 1}): {e}")
                    if attempt == max_retries - 1:
                        print("Max retries reached, aborting")
                    time.sleep(1)