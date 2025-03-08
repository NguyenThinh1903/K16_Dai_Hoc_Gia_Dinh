from itertools import cycle
from typing import NamedTuple
import time

class Player(NamedTuple):
    label: str
    color: str

class Move(NamedTuple):
    row: int
    col: int
    label: str = ""

BOARD_SIZE = 15
WIN_LENGTH = 5
DEFAULT_PLAYERS = (
    Player(label="X", color=""),
    Player(label="O", color=""),
)

class CaroGame:
    def __init__(self, players=DEFAULT_PLAYERS, board_size=BOARD_SIZE):
        self._players = cycle(players)
        self.board_size = board_size
        self.current_player = next(self._players)
        self.winner_combo = []
        self._current_moves = []
        self._has_winner = False
        self._setup_board()

    def _setup_board(self):
        self._current_moves = [
            [Move(row, col) for col in range(self.board_size)]
            for row in range(self.board_size)
        ]

    def _check_winner(self, row, col):
        label = self._current_moves[row][col].label
        directions = [(0, 1), (1, 0), (1, 1), (1, -1)]
        for dr, dc in directions:
            count = 1
            winning_cells = [(row, col)]
            for i in range(1, WIN_LENGTH):
                r, c = row + dr * i, col + dc * i
                if 0 <= r < self.board_size and 0 <= c < self.board_size and self._current_moves[r][c].label == label:
                    count += 1
                    winning_cells.append((r, c))
                else:
                    break
            for i in range(1, WIN_LENGTH):
                r, c = row - dr * i, col - dc * i
                if 0 <= r < self.board_size and 0 <= c < self.board_size and self._current_moves[r][c].label == label:
                    count += 1
                    winning_cells.append((r, c))
                else:
                    break
            if count >= WIN_LENGTH:
                self.winner_combo = winning_cells[:WIN_LENGTH]
                return True
        return False

    def toggle_player(self):
        self.current_player = next(self._players)

    def is_valid_move(self, move):
        row, col = move.row, move.col
        move_was_not_played = self._current_moves[row][col].label == ""
        no_winner = not self._has_winner
        return no_winner and move_was_not_played and 0 <= row < self.board_size and 0 <= col < self.board_size

    def process_move(self, move):
        row, col = move.row, move.col
        self._current_moves[row][col] = Move(row, col, self.current_player.label)
        if self._check_winner(row, col):
            self._has_winner = True

    def has_winner(self):
        return self._has_winner

    def is_tied(self):
        return not self._has_winner and all(
            move.label != "" for row in self._current_moves for move in row
        )

    def reset_game(self):
        self._setup_board()
        self._has_winner = False
        self.winner_combo = []

    def get_board_state(self):
        return [[self._current_moves[row][col].label for col in range(self.board_size)] 
                for row in range(self.board_size)]

    def evaluate_board(self, ai_label):
        if self.has_winner():
            return 100 if self.current_player.label == ai_label else -100
        if self.is_tied():
            return 0
        score = 0
        for row in range(self.board_size):
            for col in range(self.board_size):
                if self._current_moves[row][col].label != "":
                    label = self._current_moves[row][col].label
                    for dr, dc in [(0, 1), (1, 0), (1, 1), (1, -1)]:
                        count = 1
                        for i in range(1, WIN_LENGTH):
                            r, c = row + dr * i, col + dc * i
                            if 0 <= r < self.board_size and 0 <= c < self.board_size and self._current_moves[r][c].label == label:
                                count += 1
                            else:
                                break
                        if label == ai_label:
                            score += count * count
                        else:
                            score -= count * count
        return score

    def get_nearby_moves(self):
        moves = set()
        for row in range(self.board_size):
            for col in range(self.board_size):
                if self._current_moves[row][col].label != "":
                    for dr in [-1, 0, 1]:
                        for dc in [-1, 0, 1]:
                            r, c = row + dr, col + dc
                            if 0 <= r < self.board_size and 0 <= c < self.board_size and self._current_moves[r][c].label == "":
                                moves.add((r, c))
        return list(moves) or [(r, c) for r in range(self.board_size) for c in range(self.board_size) 
                              if self._current_moves[r][c].label == ""]

    def minimax(self, depth, alpha, beta, is_maximizing, max_depth=3, ai_label=None):
        score = self.evaluate_board(ai_label)
        if depth >= max_depth or score in [-100, 100] or self.is_tied():
            return score - depth if is_maximizing else score + depth

        moves = self.get_nearby_moves()
        start_time = time.time()
        
        if is_maximizing:
            best_score = float('-inf')
            for row, col in moves[:50]:
                if time.time() - start_time > 2:
                    break
                self._current_moves[row][col] = Move(row, col, ai_label)
                score = self.minimax(depth + 1, alpha, beta, False, max_depth, ai_label)
                self._current_moves[row][col] = Move(row, col)
                best_score = max(best_score, score)
                alpha = max(alpha, best_score)
                if beta <= alpha:
                    break
            return best_score
        else:
            best_score = float('inf')
            for row, col in moves[:50]:
                if time.time() - start_time > 2:
                    break
                opponent_label = "O" if ai_label == "X" else "X"
                self._current_moves[row][col] = Move(row, col, opponent_label)
                score = self.minimax(depth + 1, alpha, beta, True, max_depth, ai_label)
                self._current_moves[row][col] = Move(row, col)
                best_score = min(best_score, score)
                beta = min(beta, best_score)
                if beta <= alpha:
                    break
            return best_score

    def get_ai_move(self, ai_label):
        best_score = float('-inf')
        best_move = None
        alpha = float('-inf')
        beta = float('inf')
        
        moves = self.get_nearby_moves()
        if not moves:
            moves = [(r, c) for r in range(self.board_size) for c in range(self.board_size) 
                     if self._current_moves[r][c].label == ""]

        start_time = time.time()
        for row, col in moves[:50]:
            if time.time() - start_time > 2:
                break
            self._current_moves[row][col] = Move(row, col, ai_label)
            score = self.minimax(0, alpha, beta, False, ai_label=ai_label)
            self._current_moves[row][col] = Move(row, col)
            if score > best_score:
                best_score = score
                best_move = Move(row, col, ai_label)
            alpha = max(alpha, best_score)
        
        if best_move:
            print(f"AI chọn nước đi: [{best_move.row}, {best_move.col}]")
            return best_move
        print("No valid moves available!")
        return None

if __name__ == "__main__":
    game = CaroGame()
    print(game.get_board_state())