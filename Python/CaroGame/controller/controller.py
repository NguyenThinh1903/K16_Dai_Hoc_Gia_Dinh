import tkinter.messagebox as messagebox
import random
from model.game import Move, CaroGame, Player
from model.ai import CaroAI
from model.suggest_ai import SuggestAI

class CaroController:
    def __init__(self, game, board, menu):
        self._game = game
        self._board = board
        self._menu = menu
        self._ai = CaroAI(self._game)
        self._suggest_ai = SuggestAI(self._game)
        self._ai_mode = False
        self._scores = {"X": 0, "O": 0}
        self._player_label = None
        self._ai_label = None
        self._is_ai_moving = False
        self._board.update_score(self._scores)
        self._board.update_display(f"{self._game.current_player.label}'s turn")

    def handle_move(self, row, col):
        if self._is_ai_moving:
            return
        if self._ai_mode:
            if self._game.current_player.label != self._player_label:
                return
            move = Move(row, col, self._game.current_player.label)
            if not self._game.is_valid_move(move):
                messagebox.showinfo("Invalid Move", "Position taken or game over!", parent=self._board)
                return
            self._game.process_move(move)
            self._board.update_button(row, col, self._game.current_player.label)
            self._check_game_state()
            if not self._game.has_winner() and not self._game.is_tied():
                self._game.toggle_player()
                self._is_ai_moving = True
                self._execute_ai_move()
        else:
            move = Move(row, col, self._game.current_player.label)
            if not self._game.is_valid_move(move):
                messagebox.showinfo("Invalid Move", "Position taken or game over!", parent=self._board)
                return
            self._game.process_move(move)
            self._board.update_button(row, col, self._game.current_player.label)
            self._check_game_state()
            if not self._game.has_winner() and not self._game.is_tied():
                self._game.toggle_player()
                self._board.update_display(f"Player {self._game.current_player.label}'s turn")

    def _execute_ai_move(self):
        self._board.update_display(f"AI's turn ({self._ai_label}) - Thinking...", "cyan")
        self._board.after(1000, self._process_ai_move)

    def _process_ai_move(self):
        ai_move = self._ai.get_ai_move(self._ai_label)
        if ai_move:
            self._game.process_move(ai_move)
            self._board.update_button(ai_move.row, ai_move.col, ai_move.label)
            self._check_game_state()
            if not self._game.has_winner() and not self._game.is_tied():
                self._game.toggle_player()
                self._board.update_display(f"Your turn ({self._player_label})")
        self._is_ai_moving = False

    def _check_game_state(self):
        if self._game.has_winner():
            self._board.highlight_cells(self._game.winner_combo)
            winner = self._game.current_player.label
            self._scores[winner] += 1
            msg = f"{winner} won!" if not self._ai_mode else ("AI won!" if winner == self._ai_label else "You won!")
            self._board.update_display(msg, "yellow")
            self._board.update_score(self._scores)
        elif self._game.is_tied():
            self._board.update_display("Tied game!", "red")
            self._board.update_score(self._scores)

    def set_ai_mode(self, enable=True):
        self._ai_mode = enable
        self._game.reset_game()
        self._board.reset_board()
        self._is_ai_moving = False
        if enable:
            labels = ["X", "O"]
            self._ai_label = random.choice(labels)
            self._player_label = "O" if self._ai_label == "X" else "X"
            start_player = random.choice(["AI", "Player"])
            if start_player == "AI":
                self._game.current_player = Player(label=self._ai_label, color="")
                self._board.update_display(f"AI's turn ({self._ai_label})")
                self._is_ai_moving = True
                self._execute_ai_move()
            else:
                self._game.current_player = Player(label=self._player_label, color="")
                self._board.update_display(f"Your turn ({self._player_label})")
        else:
            self._board.update_display(f"Player {self._game.current_player.label}'s turn")

    def reset_game(self):
        self._game.reset_game()
        self._board.reset_board()
        self._is_ai_moving = False
        if self._ai_mode:
            self.set_ai_mode(True)
        else:
            self._board.update_display(f"Player {self._game.current_player.label}'s turn")
        self._board.update_score(self._scores)

    def back_to_menu(self):
        self._board.withdraw()
        self._menu.deiconify()

    def start(self):
        pass

    def suggest_move(self):
        if self._game.has_winner() or self._game.is_tied():
            messagebox.showinfo("Game Over", "Game over, no suggestions available!", parent=self._board)
        elif self._ai_mode and self._game.current_player.label == self._ai_label:
            messagebox.showinfo("Invalid", "Cannot suggest for AI!", parent=self._board)
        else:
            self._board.update_display("Suggesting move - Thinking...", "cyan")
            move = self._suggest_ai.get_suggested_move(self._game.current_player.label)
            self._board.update_display(f"Your turn ({self._game.current_player.label})")
            if move:
                self._board.highlight_suggested_move(move.row, move.col)