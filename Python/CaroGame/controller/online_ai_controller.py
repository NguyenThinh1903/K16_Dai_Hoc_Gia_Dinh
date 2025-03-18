# controller/online_ai_controller.py
import tkinter.messagebox as messagebox
from model.suggest_ai import SuggestAI
from model.network_service import NetworkService
from model.online_game_manager import OnlineGameManager
from model.game import Move


class OnlineAIController:
    def __init__(self, network_service: NetworkService, game_manager: OnlineGameManager, board):
        self.suggest_ai = SuggestAI(game_manager.game)  # Pass the CaroGame instance
        self.network_service = network_service
        self.game_manager = game_manager
        self.board = board  # Add board for displaying suggestions

    def request_suggestion(self):
        if self.game_manager.game.has_winner() or self.game_manager.game.is_tied():
            messagebox.showinfo("Game Over", "Game over, no suggestions available!", parent=self.board)
            return

        if self.game_manager.current_player_label != self.game_manager.my_label:
            messagebox.showinfo("Wait", "Not your turn!", parent=self.board)
            return

        current_player = self.game_manager.current_player_label
        board_state = self.game_manager.game.get_board_state()
        suggested_move = self.suggest_ai.get_suggested_move(current_player)

        if suggested_move:
            self.handle_suggestion_locally(suggested_move.row, suggested_move.col)

    def handle_suggestion_locally(self, row, col):
        """Handles displaying the suggestion directly on the board."""
        self.board.highlight_suggested_move(row, col)