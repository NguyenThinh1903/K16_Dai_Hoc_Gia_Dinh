import tkinter.messagebox as messagebox
import random
from game import Move, CaroGame, Player
from board import CaroBoard

class CaroController:
    def __init__(self, game, board=None):
        self._game = game
        self._board = board if board else CaroBoard(self)
        self._ai_mode = False
        self._scores = {"X": 0, "O": 0}
        self._player_label = None
        self._ai_label = None
        self._is_ai_moving = False
        self._board.update_score(self._scores)
        self._board.update_display(f"{self._game.current_player.label}'s turn")

    def handle_move(self, row, col):
        try:
            move = Move(row, col, self._game.current_player.label)
            print(f"Handling move at [{row}, {col}] with label {self._game.current_player.label}")
            if self._game.is_valid_move(move):
                self._game.process_move(move)
                self._board.update_button(row, col, self._game.current_player.label)

                if self._game.has_winner():
                    self._board.highlight_cells(self._game.winner_combo)
                    winner = self._game.current_player.label
                    self._scores[winner] += 1
                    msg = f'Player "{winner}" won!'
                    self._board.update_display(msg, "yellow")
                    self._board.update_score(self._scores)
                elif self._game.is_tied():
                    msg = "Tied game!"
                    self._board.update_display(msg, "red")
                    self._board.update_score(self._scores)
                else:
                    self._game.toggle_player()
                    self._is_ai_moving = False  # Reset cờ sau khi người chơi đi
                    display_msg = "Your turn" if self._game.current_player.label == self._player_label else "AI's turn"
                    self._board.update_display(f"{display_msg} ({self._game.current_player.label})")
                    if self._ai_mode and self._game.current_player.label == self._ai_label and not self._game.has_winner():
                        self._is_ai_moving = True
                        self._board.after(500, self._play_ai_move)
        except Exception as e:
            messagebox.showerror("Error", f"An error occurred: {e}", parent=self._board)

    def _play_ai_move(self):
        if not self._game.has_winner() and self._is_ai_moving:
            ai_move = self._game.get_ai_move(self._ai_label)
            if ai_move is not None:
                print(f"AI move processed: {ai_move}")
                self.handle_move(ai_move.row, ai_move.col)
            self._is_ai_moving = False

    def reset_game(self):
        self._game.reset_game()
        self._board.reset_board()
        self._is_ai_moving = False
        display_msg = "Your turn" if self._game.current_player.label == self._player_label else "AI's turn"
        self._board.update_display(f"{display_msg} ({self._game.current_player.label})")
        self._board.update_score(self._scores)
        self._board.update_idletasks()
        if self._ai_mode and self._game.current_player.label == self._ai_label and not self._game.has_winner():
            self._is_ai_moving = True
            self._board.after(500, self._play_ai_move)

    def set_ai_mode(self, enable=True):
        self._ai_mode = enable
        if enable:
            labels = ["X", "O"]
            self._ai_label = random.choice(labels)
            labels.remove(self._ai_label)
            self._player_label = labels[0]
            start_player = random.choice(["AI", "Player"])
            if start_player == "AI":
                self._game.current_player = Player(label=self._ai_label, color="")
                messagebox.showinfo("Mode", f"AI ({self._ai_label}) will start. Wait for AI's move. You are {self._player_label}.", parent=self._board)
            else:
                self._game.current_player = Player(label=self._player_label, color="")
                messagebox.showinfo("Mode", f"You ({self._player_label}) will start. Place your move. AI is {self._ai_label}.", parent=self._board)
            self.reset_game()
        else:
            self.reset_game()

    def back_to_menu(self):
        self._board.destroy()
        from menu import StartMenu
        menu = StartMenu()
        menu.mainloop()

    def start(self):
        self._board.mainloop()

    def get_scores(self):
        return self._scores