from tkinter import messagebox
from game import Move  # Import Move từ game.py

class TicTacToeController:
    def __init__(self, game, board):
        self._game = game
        self._board = board
        self._ai_mode = False

    def handle_move(self, row, col):
        move = Move(row, col, self._game.current_player.label)
        if self._game.is_valid_move(move):
            self._game.process_move(move)
            self._board.update_button(row, col, self._game.current_player.label, self._game.current_player.color)

            if self._game.has_winner():
                self._board.highlight_cells(self._game.winner_combo)
                msg = f'Player "{self._game.current_player.label}" won!'
                self._board.update_display(msg, self._game.current_player.color)
            elif self._game.is_tied():
                self._board.update_display("Tied game!", "red")
            else:
                self._game.toggle_player()
                self._board.update_display(f"{self._game.current_player.label}'s turn")
                if self._ai_mode and not self._game.has_winner():
                    self._play_ai_move()

    def _play_ai_move(self):
        ai_move = self._game.get_ai_move()
        if ai_move:
            self._game.process_move(Move(ai_move.row, ai_move.col, self._game.current_player.label))
            self._board.update_button(ai_move.row, ai_move.col, self._game.current_player.label, self._game.current_player.color)

            if self._game.has_winner():
                self._board.highlight_cells(self._game.winner_combo)
                self._board.update_display("AI won!", self._game.current_player.color)
            elif self._game.is_tied():
                self._board.update_display("Tied game!", "red")
            else:
                self._game.toggle_player()
                self._board.update_display(f"{self._game.current_player.label}'s turn")

    def reset_game(self):
        self._game.reset_game()
        self._board.reset_board()

    def set_ai_mode(self):
        self._ai_mode = True
        messagebox.showinfo("Mode", "Now playing against AI!")
        self.reset_game()