# model/online_game_manager.py
from model.game import CaroGame, Player, Move

class OnlineGameManager:
    def __init__(self, game: CaroGame, board, is_host=False):
        self.game = game
        self.board = board
        self.scores = {"X": 0, "O": 0}
        self.current_player_label = None
        self.is_host = is_host
        self.board.update_score(self.scores)
        self._game_ended = False

    def set_players(self, my_label, opponent_label):
        self.my_label = my_label
        self.opponent_label = opponent_label
        self.current_player_label = "X"
        self.game.current_player = Player(label=self.current_player_label, color="")
        self._game_ended = False

    def handle_move(self, row, col, player_label):
        move = Move(row, col, player_label)
        if not self.game.is_valid_move(move):
            return False

        self.game.process_move(move)
        self.board.update_button(row, col, player_label)
        game_state = self._check_game_state()
        if game_state == "continue":
            self.game.toggle_player()
            self.current_player_label = self.game.current_player.label
            print(f"Updating button at [{row}, {col}] with label {player_label}")
            print(f"Toggled player to: {self.current_player_label}")
            self.board.update_display(f"{'Your' if self.current_player_label == self.my_label else 'Opponent\'s'} turn ({self.current_player_label})")
        return True

    def process_opponent_move(self, row, col, opponent_label):
        move = Move(row, col, opponent_label)
        self.game.process_move(move)
        self.board.update_button(row, col, opponent_label)
        game_state = self._check_game_state()
        if game_state == "continue":
            self.game.toggle_player()
            self.current_player_label = self.game.current_player.label
            print(f"Opponent moved at [{row}, {col}], toggled to: {self.current_player_label}")
            self.board.update_display(f"{'Your' if self.current_player_label == self.my_label else 'Opponent\'s'} turn ({self.current_player_label})")

    def _check_game_state(self):
        if self._game_ended:
            return "game_over"

        if self.game.has_winner():
            self._game_ended = True
            self.board.highlight_cells(self.game.winner_combo)
            winner = self.game.current_player.label
            if self.is_host:
                self.scores[winner] += 1
                self.board.update_score(self.scores)
                self.board._controller.send_message(
                    "game_over",
                    winner=winner,
                    scores=self.scores
                )
            self.board.update_display(f"{winner} won!", "yellow")
            return "game_over"
        elif self.game.is_tied():
            self._game_ended = True
            if self.is_host:
                self.board.update_score(self.scores)
                self.board._controller.send_message(
                    "game_over",
                    winner=None,
                    scores=self.scores
                )
            self.board.update_display("Tied game!", "red")
            return "game_over"
        return "continue"

    def update_score_from_host(self, scores):
        self.scores = scores
        self.board.update_score(self.scores)

    def reset_game(self):
        self.game.reset_game()
        self.board.reset_board()
        self.current_player_label = "X"
        self.game.current_player = Player(label=self.current_player_label, color="")
        self._game_ended = False
        self.board.update_display(f"{'Your' if self.current_player_label == self.my_label else 'Opponent\'s'} turn ({self.current_player_label})")
        self.board.update_score(self.scores)

    def update_turn(self, next_player_label):
        self.current_player_label = next_player_label
        self.game.current_player = Player(label=next_player_label, color="")
        self.board.update_display(f"{'Your' if self.current_player_label == self.my_label else 'Opponent\'s'} turn ({self.current_player_label})")