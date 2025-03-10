import tkinter.messagebox as messagebox
import random
from model.game import Move, CaroGame, Player
from model.ai import CaroAI

class CaroController:
    def __init__(self, game, board, menu):
        self._game = game
        self._board = board
        self._menu = menu
        self._ai = CaroAI(self._game)
        self._ai_mode = False
        self._scores = {"X": 0, "O": 0}
        self._player_label = None
        self._ai_label = None
        self._is_ai_moving = False
        self._board.update_score(self._scores)
        self._board.update_display(f"{self._game.current_player.label}'s turn")
        print(f"Initialized controller: AI mode = {self._ai_mode}, Current player = {self._game.current_player.label}")

    def handle_move(self, row, col):
        if self._is_ai_moving:
            print("Blocked move: AI is currently moving.")
            return
        try:
            print(f"Handling move at [{row}, {col}] with label {self._game.current_player.label}")
            if self._ai_mode and self._game.current_player.label != self._player_label:
                print("Invalid move: It's AI's turn, not player's turn.")
                return
            move = Move(row, col, self._game.current_player.label)
            if not self._game.is_valid_move(move):
                print(f"Invalid move at [{row}, {col}]: Position already taken or game over.")
                messagebox.showinfo("Invalid Move", "This position is already taken or the game is over!", parent=self._board)
                return
            self._game.process_move(move)
            self._board.update_button(row, col, self._game.current_player.label)

            if self._game.has_winner():
                self._board.highlight_cells(self._game.winner_combo)
                winner = self._game.current_player.label
                self._scores[winner] += 1
                if self._ai_mode:
                    if winner == self._ai_label:
                        msg = "AI won!"
                    else:
                        msg = "You won!"
                else:
                    msg = f"Player {winner} won!"
                self._board.update_display(msg, "yellow")
                self._board.update_score(self._scores)
                print(f"Game over: {msg}")
            elif self._game.is_tied():
                msg = "Tied game!"
                self._board.update_display(msg, "red")
                self._board.update_score(self._scores)
                print(f"Game over: {msg}")
            else:
                self._game.toggle_player()
                self._is_ai_moving = False
                if self._ai_mode:
                    display_msg = "Your turn" if self._game.current_player.label == self._player_label else "AI's turn"
                    self._board.update_display(f"{display_msg} ({self._game.current_player.label})")
                    print(f"Turn switched to: {display_msg} ({self._game.current_player.label})")
                    if self._game.current_player.label == self._ai_label and not self._game.has_winner():
                        self._is_ai_moving = True
                        print(f"AI turn triggered, is_ai_moving = {self._is_ai_moving}")
                        self._execute_ai_move()
                else:
                    display_msg = f"Player {self._game.current_player.label}'s turn"
                    self._board.update_display(f"{display_msg} ({self._game.current_player.label})")
                    print(f"Turn switched to: {display_msg} ({self._game.current_player.label})")
        except Exception as e:
            messagebox.showerror("Error", f"An error occurred: {e}", parent=self._board)
            print(f"Error in handle_move: {e}")
            self._is_ai_moving = False

    def _execute_ai_move(self):
        try:
            if not self._game.has_winner() and self._is_ai_moving and self._game.current_player.label == self._ai_label:
                print(f"Starting AI move for {self._ai_label}, is_ai_moving = {self._is_ai_moving}")
                self._board.update_display(f"AI's turn ({self._ai_label}) - Thinking...", "cyan")
                self._board.update_idletasks()
                # Thêm độ trễ 1 giây để hiển thị thông báo "AI's turn - Thinking..."
                self._board.after(1000, self._process_ai_move)
        except Exception as e:
            print(f"Error in execute_ai_move: {e}")
            self._is_ai_moving = False

    def _process_ai_move(self):
        try:
            ai_move = self._ai.get_ai_move(self._ai_label)
            if ai_move is not None:
                print(f"AI selected move: [{ai_move.row}, {ai_move.col}] with label {self._ai_label}")
                self._game.process_move(ai_move)
                self._board.update_button(ai_move.row, ai_move.col, ai_move.label)

                if self._game.has_winner():
                    self._board.highlight_cells(self._game.winner_combo)
                    winner = self._game.current_player.label
                    self._scores[winner] += 1
                    if self._ai_mode:
                        if winner == self._ai_label:
                            msg = "AI won!"
                        else:
                            msg = "You won!"
                    else:
                        msg = f"Player {winner} won!"
                    self._board.update_display(msg, "yellow")
                    self._board.update_score(self._scores)
                    print(f"Game over: {msg}")
                elif self._game.is_tied():
                    msg = "Tied game!"
                    self._board.update_display(msg, "red")
                    self._board.update_score(self._scores)
                    print(f"Game over: {msg}")
                else:
                    self._game.toggle_player()
                    if self._ai_mode:
                        display_msg = "Your turn" if self._game.current_player.label == self._player_label else "AI's turn"
                    else:
                        display_msg = f"Player {self._game.current_player.label}'s turn"
                    self._board.update_display(f"{display_msg} ({self._game.current_player.label})")
                    print(f"AI move executed, turn switched to: {display_msg} ({self._game.current_player.label})")
            else:
                messagebox.showinfo("AI Error", "AI could not find a valid move!", parent=self._board)
                print("AI could not find a valid move")
                self._game.toggle_player()
                self._board.update_display(f"Your turn ({self._player_label})")
        finally:
            self._is_ai_moving = False

    def reset_game(self):
        self._game.reset_game()
        self._board.reset_board()
        self._is_ai_moving = False
        if self._ai_mode:
            display_msg = "Your turn" if self._game.current_player.label == self._player_label else "AI's turn"
            self._board.update_display(f"{display_msg} ({self._game.current_player.label})")
            if self._game.current_player.label == self._ai_label and not self._game.has_winner():
                self._is_ai_moving = True
                print(f"AI turn triggered after reset, is_ai_moving = {self._is_ai_moving}")
                self._execute_ai_move()
        else:
            display_msg = f"Player {self._game.current_player.label}'s turn"
            self._board.update_display(f"{display_msg} ({self._game.current_player.label})")
        self._board.update_score(self._scores)
        print(f"Game reset, current player = {self._game.current_player.label}, display_msg = {display_msg}")

    def set_ai_mode(self, enable=True):
        self._ai_mode = enable
        self._game.reset_game()
        self._board.reset_board()
        self._is_ai_moving = False
        if enable:
            labels = ["X", "O"]
            self._ai_label = random.choice(labels)
            labels.remove(self._ai_label)
            self._player_label = labels[0]
            start_player = random.choice(["AI", "Player"])
            if start_player == "AI":
                self._game.current_player = Player(label=self._ai_label, color="")
                self._board.update_display(f"AI's turn ({self._ai_label})", "cyan")
                messagebox.showinfo("Mode", f"AI ({self._ai_label}) will start. Wait for AI's move. You are {self._player_label}.", parent=self._board)
                self._is_ai_moving = True
                print(f"AI starting game, is_ai_moving = {self._is_ai_moving}")
                self._execute_ai_move()
                self._board.update_idletasks()
            else:
                self._game.current_player = Player(label=self._player_label, color="")
                self._board.update_display(f"Your turn ({self._player_label})")
                messagebox.showinfo("Mode", f"You ({self._player_label}) will start. Place your move. AI is {self._ai_label}.", parent=self._board)
            print(f"AI mode set, ai_label = {self._ai_label}, player_label = {self._player_label}")
        else:
            self._board.update_display(f"Player {self._game.current_player.label}'s turn")
            print("AI mode disabled")

    def back_to_menu(self):
        self._board.withdraw()
        self._menu.deiconify()

    def start(self):
        pass  # Đã xóa mainloop để dùng chung với StartMenu

    def get_scores(self):
        return self._scores

    def suggest_move(self):
        if self._game.has_winner() or self._game.is_tied():
            messagebox.showinfo("Game Over", "Trò chơi đã kết thúc, không thể gợi ý nước đi!", parent=self._board)
            return
        if self._ai_mode and self._game.current_player.label == self._ai_label:
            messagebox.showinfo("Invalid Request", "Không thể gợi ý nước đi cho AI!", parent=self._board)
            return
        if self._is_ai_moving:
            messagebox.showinfo("Wait", "Đợi AI hoàn thành nước đi trước khi gợi ý!", parent=self._board)
            return

        player_label = self._game.current_player.label
        move = self._ai.suggest_move(player_label)
        if move:
            self._board.highlight_suggested_move(move.row, move.col)
            print(f"Suggested move for player {player_label}: [{move.row}, {move.col}]")
        else:
            messagebox.showinfo("No Suggestion", "Không tìm thấy nước đi gợi ý!", parent=self._board)