from itertools import cycle
from typing import NamedTuple
import requests
from dotenv import load_dotenv
import os

# Tải biến môi trường từ file .env
load_dotenv()

class Player(NamedTuple):
    label: str
    color: str

class Move(NamedTuple):
    row: int
    col: int
    label: str = ""

BOARD_SIZE = 3
DEFAULT_PLAYERS = (
    Player(label="X", color="blue"),
    Player(label="O", color="green"),
)

class TicTacToeGame:
    def __init__(self, players=DEFAULT_PLAYERS, board_size=BOARD_SIZE):
        self._players = cycle(players)
        self.board_size = board_size
        self.current_player = next(self._players)
        self.winner_combo = []
        self._current_moves = []
        self._has_winner = False
        self._winning_combos = []
        self._setup_board()

    def _setup_board(self):
        """Khởi tạo bàn cờ trống."""
        self._current_moves = [
            [Move(row, col) for col in range(self.board_size)]
            for row in range(self.board_size)
        ]
        self._winning_combos = self._get_winning_combos()

    def _get_winning_combos(self):
        """Trả về tất cả các tổ hợp thắng có thể."""
        rows = [[(move.row, move.col) for move in row] for row in self._current_moves]
        columns = [list(col) for col in zip(*rows)]
        first_diagonal = [row[i] for i, row in enumerate(rows)]
        second_diagonal = [col[j] for j, col in enumerate(reversed(columns))]
        return rows + columns + [first_diagonal, second_diagonal]

    def toggle_player(self):
        """Chuyển lượt người chơi."""
        self.current_player = next(self._players)

    def is_valid_move(self, move):
        """Kiểm tra nước đi hợp lệ."""
        row, col = move.row, move.col
        move_was_not_played = self._current_moves[row][col].label == ""
        no_winner = not self._has_winner
        return no_winner and move_was_not_played

    def process_move(self, move):
        """Xử lý nước đi và kiểm tra thắng."""
        row, col = move.row, move.col
        self._current_moves[row][col] = move
        for combo in self._winning_combos:
            results = set(self._current_moves[n][m].label for n, m in combo)
            is_win = (len(results) == 1) and ("" not in results)
            if is_win:
                self._has_winner = True
                self.winner_combo = combo
                break

    def has_winner(self):
        """Kiểm tra có người thắng chưa."""
        return self._has_winner

    def is_tied(self):
        """Kiểm tra trận hòa."""
        no_winner = not self._has_winner
        played_moves = (move.label for row in self._current_moves for move in row)
        return no_winner and all(played_moves)

    def reset_game(self):
        """Đặt lại bàn cờ."""
        for row in range(self.board_size):
            for col in range(self.board_size):
                self._current_moves[row][col] = Move(row, col)
        self._has_winner = False
        self.winner_combo = []
        self.current_player = next(self._players)

    def get_available_moves(self):
        """Trả về danh sách nước đi khả dụng."""
        return [
            Move(row, col)
            for row in range(self.board_size)
            for col in range(self.board_size)
            if self._current_moves[row][col].label == ""
        ]

    def get_board_state(self):
        """Chuyển bàn cờ thành định dạng gửi qua API."""
        return [[self._current_moves[row][col].label for col in range(self.board_size)] 
                for row in range(self.board_size)]

    def get_ai_move(self):
        """Gọi API để lấy nước đi cho AI."""
        board_state = self.get_board_state()
        
        # Lấy thông tin từ file .env
        api_url = os.getenv("API_URL")
        api_key = os.getenv("API_KEY")

        if not api_url or not api_key:
            print("API URL or Key not found, falling back to random move")
            import random
            moves = self.get_available_moves()
            return random.choice(moves) if moves else None

        payload = {
            "board": board_state,
            "player": self.current_player.label  # AI là "O"
        }

        try:
            response = requests.post(api_url, json=payload, headers={"Authorization": f"Bearer {api_key}"})
            response.raise_for_status()
            data = response.json()
            
            # Giả sử API trả về {"row": 1, "col": 2}
            row, col = data["row"], data["col"]
            move = Move(row, col)
            if self.is_valid_move(move):
                return move
            else:
                raise ValueError("Invalid move from API")
        except Exception as e:
            print(f"API error: {e}")
            import random
            moves = self.get_available_moves()
            return random.choice(moves) if moves else None

if __name__ == "__main__":
    game = TicTacToeGame()
    print(game.get_board_state())