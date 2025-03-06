import tkinter.messagebox as messagebox
from game import Move, TicTacToeGame, Player
from board import TicTacToeBoard

class TicTacToeController:
    def __init__(self, game, board=None):
        self._game = game
        self._board = board if board else TicTacToeBoard(self)
        self._ai_mode = False
        self._scores = {"X": 0, "O": 0}
        self._board.update_score(self._scores)

    def handle_move(self, row, col):
        try:
            move = Move(row, col, self._game.current_player.label)
            if self._game.is_valid_move(move):
                self._game.process_move(move)
                self._board.update_button(row, col, self._game.current_player.label, self._game.current_player.color)

                if self._game.has_winner():
                    self._board.highlight_cells(self._game.winner_combo)
                    winner = self._game.current_player.label
                    self._scores[winner] += 1
                    msg = f'Player "{winner}" won!'
                    self._board.update_display(msg, self._game.current_player.color)
                    self._board.update_score(self._scores)
                elif self._game.is_tied():
                    msg = "Tied game!"
                    self._board.update_display(msg, "red")
                    self._board.update_score(self._scores)
                else:
                    self._game.toggle_player()
                    self._board.update_display(f"{self._game.current_player.label}'s turn")
                    self._board.update_score(self._scores)
                    if self._ai_mode and self._game.current_player.label == "O" and not self._game.has_winner():
                        self._play_ai_move()
        except Exception as e:
            messagebox.showerror("Error", f"An error occurred: {e}", parent=self._board)

    def _play_ai_move(self):
        """Xử lý nước đi của AI với độ trễ."""
        ai_move = self._game.get_ai_move()
        if ai_move:
            # Thêm độ trễ 1000ms (1 giây) trước khi AI đánh
            self._board.after(1000, lambda: self.handle_move(ai_move.row, ai_move.col))
        else:
            messagebox.showwarning("Warning", "AI could not find a valid move!", parent=self._board)

    def reset_game(self):
        self._game.reset_game()
        self._board.reset_board()
        self._board.update_display(f"{self._game.current_player.label}'s turn")
        self._board.update_score(self._scores)
        if self._ai_mode and self._game.current_player.label == "O" and not self._game.has_winner():
            self._play_ai_move()

    def set_ai_mode(self, enable=True):
        self._ai_mode = enable
        if enable:
            messagebox.showinfo("Mode", "Now playing against AI!", parent=self._board)
        self.reset_game()
        # Nếu sau khi reset, lượt đầu là của AI (O), thêm delay cho lượt đầu tiên
        if self._ai_mode and self._game.current_player.label == "O" and not self._game.has_winner():
            self._play_ai_move()

    def back_to_menu(self):
        self._board.destroy()
        from menu import StartMenu
        menu = StartMenu()
        menu.mainloop()

    def start(self):
        self._board.mainloop()

    def get_scores(self):
        return self._scores