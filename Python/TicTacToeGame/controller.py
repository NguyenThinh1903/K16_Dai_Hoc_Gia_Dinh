import tkinter.messagebox as messagebox
from game import Move, TicTacToeGame, Player
from board import TicTacToeBoard  # Thêm import này

class TicTacToeController:
    def __init__(self, game, board=None):
        """Khởi tạo controller với game và board tùy chọn."""
        self._game = game
        self._board = board if board else TicTacToeBoard(self)  # Board có thể truyền từ ngoài
        self._ai_mode = False

    def handle_move(self, row, col):
        """Xử lý nước đi của người chơi hoặc AI."""
        try:
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
                    # Nếu chế độ AI và là lượt của AI (O)
                    if self._ai_mode and self._game.current_player.label == "O" and not self._game.has_winner():
                        self._play_ai_move()
        except Exception as e:
            messagebox.showerror("Error", f"An error occurred: {e}")

    def _play_ai_move(self):
        """Xử lý nước đi của AI."""
        ai_move = self._game.get_ai_move()
        if ai_move:
            self.handle_move(ai_move.row, ai_move.col)
        else:
            messagebox.showwarning("Warning", "AI could not find a valid move!")

    def reset_game(self):
        """Đặt lại trò chơi."""
        self._game.reset_game()
        self._board.reset_board()
        self._board.update_display(f"{self._game.current_player.label}'s turn")

    def set_ai_mode(self, enable=True):
        """Bật/tắt chế độ AI."""
        self._ai_mode = enable
        if enable:
            messagebox.showinfo("Mode", "Now playing against AI!")
        self.reset_game()

    def back_to_menu(self):
        """Quay lại menu chính."""
        self._board.destroy()
        from menu import StartMenu
        menu = StartMenu()
        menu.mainloop()

    def start(self):
        """Khởi động giao diện."""
        self._board.mainloop()