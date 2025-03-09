from typing import NamedTuple

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
        self._players = players  # Lưu danh sách người chơi
        self.board_size = board_size
        self.current_player = self._players[0]  # Người chơi đầu tiên (X)
        self.winner_combo = []
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
                    winning_cells.insert(0, (r, c))
                else:
                    break
            if count >= WIN_LENGTH:
                winning_cells.sort(key=lambda x: (x[0], x[1]))
                self.winner_combo = winning_cells[:WIN_LENGTH]
                return True
        return False

    def toggle_player(self):
        # Chuyển đổi thủ công giữa hai người chơi
        self.current_player = self._players[1] if self.current_player.label == self._players[0].label else self._players[0]
        print(f"Toggled player to: {self.current_player.label}")

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
        self.current_player = self._players[0]  # Đặt lại người chơi đầu tiên (X)
        print(f"Game reset, current player = {self.current_player.label}")

    def get_board_state(self):
        return [[self._current_moves[row][col].label for col in range(self.board_size)] 
                for row in range(self.board_size)]

    def get_nearby_moves(self, distance=1):
        moves = set()
        for row in range(self.board_size):
            for col in range(self.board_size):
                if self._current_moves[row][col].label != "":
                    for dr in range(-distance, distance + 1):
                        for dc in range(-distance, distance + 1):
                            r, c = row + dr, col + dc
                            if 0 <= r < self.board_size and 0 <= c < self.board_size and self._current_moves[r][c].label == "":
                                moves.add((r, c))
        return list(moves) or [(self.board_size // 2, self.board_size // 2)]