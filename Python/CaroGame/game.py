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
            return 1000 if self.current_player.label == ai_label else -1000
        if self.is_tied():
            return 0
        score = 0
        opponent_label = "O" if ai_label == "X" else "X"

        for row in range(self.board_size):
            for col in range(self.board_size):
                if self._current_moves[row][col].label != "":
                    label = self._current_moves[row][col].label
                    for dr, dc in [(0, 1), (1, 0), (1, 1), (1, -1)]:
                        count = 1
                        open_ends = 0
                        # Kiểm tra phía trước
                        for i in range(1, WIN_LENGTH):
                            r, c = row + dr * i, col + dc * i
                            if 0 <= r < self.board_size and 0 <= c < self.board_size and self._current_moves[r][c].label == label:
                                count += 1
                            else:
                                break
                        if count < WIN_LENGTH:
                            r, c = row + dr * count, col + dc * count
                            if 0 <= r < self.board_size and 0 <= c < self.board_size and self._current_moves[r][c].label == "":
                                open_ends += 1
                        # Kiểm tra phía sau
                        for i in range(1, WIN_LENGTH):
                            r, c = row - dr * i, col - dc * i
                            if 0 <= r < self.board_size and 0 <= c < self.board_size and self._current_moves[r][c].label == label:
                                count += 1
                            else:
                                break
                        if count < WIN_LENGTH:
                            r, c = row - dr * count, col - dc * count
                            if 0 <= r < self.board_size and 0 <= c < self.board_size and self._current_moves[r][c].label == "":
                                open_ends += 1
                        # Tăng trọng số
                        weight = count * count * 10
                        if count >= 3:  # Tăng trọng số cho hàng 3
                            weight *= 10  # Tăng từ x7 lên x10
                        if count == 4:  # Đặc biệt ưu tiên hàng 4
                            weight *= 25  # Tăng từ x15 lên x25
                        if open_ends > 0:  # Hàng mở có giá trị cao hơn
                            weight *= 3  # Giữ x3
                        if label == ai_label:
                            score += weight
                            if count >= 3 and open_ends > 0:  # Thưởng cho AI tạo hàng dài mở
                                score += 200  # Tăng từ 150 lên 200
                        else:
                            score -= weight * 2  # Tăng từ x1.5 lên x2
                            if count >= 3 and open_ends > 0:  # Phạt nếu đối thủ có hàng dài mở
                                score -= 200  # Tăng từ 150 lên 200
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

    def check_opponent_winning_move(self, ai_label):
        """Kiểm tra và chặn các hàng dài của đối thủ (3 hoặc 4 ô, hoặc 2 ô mở ở vị trí chiến lược)."""
        opponent_label = "O" if ai_label == "X" else "X"
        moves = self.get_nearby_moves()

        # Kiểm tra hàng 4 ô (ưu tiên chặn ngay)
        for row, col in moves:
            if self._current_moves[row][col].label == "":
                self._current_moves[row][col] = Move(row, col, opponent_label)
                directions = [(0, 1), (1, 0), (1, 1), (1, -1)]
                for dr, dc in directions:
                    count = 1
                    for i in range(1, WIN_LENGTH):
                        r, c = row + dr * i, col + dc * i
                        if 0 <= r < self.board_size and 0 <= c < self.board_size and self._current_moves[r][c].label == opponent_label:
                            count += 1
                        else:
                            break
                    for i in range(1, WIN_LENGTH):
                        r, c = row - dr * i, col - dc * i
                        if 0 <= r < self.board_size and 0 <= c < self.board_size and self._current_moves[r][c].label == opponent_label:
                            count += 1
                        else:
                            break
                    if count >= 4:
                        self._current_moves[row][col] = Move(row, col)
                        return Move(row, col, ai_label)
                self._current_moves[row][col] = Move(row, col)

        # Kiểm tra hàng 3 ô
        for row, col in moves:
            if self._current_moves[row][col].label == "":
                self._current_moves[row][col] = Move(row, col, opponent_label)
                directions = [(0, 1), (1, 0), (1, 1), (1, -1)]
                for dr, dc in directions:
                    count = 1
                    open_ends = 0
                    for i in range(1, WIN_LENGTH - 1):
                        r, c = row + dr * i, col + dc * i
                        if 0 <= r < self.board_size and 0 <= c < self.board_size and self._current_moves[r][c].label == opponent_label:
                            count += 1
                        else:
                            break
                    if count < 4:
                        r, c = row + dr * count, col + dc * count
                        if 0 <= r < self.board_size and 0 <= c < self.board_size and self._current_moves[r][c].label == "":
                            open_ends += 1
                    for i in range(1, WIN_LENGTH - 1):
                        r, c = row - dr * i, col - dc * i
                        if 0 <= r < self.board_size and 0 <= c < self.board_size and self._current_moves[r][c].label == opponent_label:
                            count += 1
                        else:
                            break
                    if count < 4:
                        r, c = row - dr * count, col - dc * count
                        if 0 <= r < self.board_size and 0 <= c < self.board_size and self._current_moves[r][c].label == "":
                            open_ends += 1
                    if count == 3 and open_ends > 0:
                        self._current_moves[row][col] = Move(row, col)
                        return Move(row, col, ai_label)
                self._current_moves[row][col] = Move(row, col)

        # Kiểm tra hàng 2 ô mở ở vị trí trung tâm (chiến lược)
        center_row = self.board_size // 2
        center_col = self.board_size // 2
        for row, col in moves:
            if abs(row - center_row) <= 2 and abs(col - center_col) <= 2:  # Chỉ kiểm tra gần trung tâm
                if self._current_moves[row][col].label == "":
                    self._current_moves[row][col] = Move(row, col, opponent_label)
                    directions = [(0, 1), (1, 0), (1, 1), (1, -1)]
                    for dr, dc in directions:
                        count = 1
                        open_ends = 0
                        for i in range(1, 3):
                            r, c = row + dr * i, col + dc * i
                            if 0 <= r < self.board_size and 0 <= c < self.board_size and self._current_moves[r][c].label == opponent_label:
                                count += 1
                            else:
                                break
                        if count < 3:
                            r, c = row + dr * count, col + dc * count
                            if 0 <= r < self.board_size and 0 <= c < self.board_size and self._current_moves[r][c].label == "":
                                open_ends += 1
                        for i in range(1, 3):
                            r, c = row - dr * i, col - dc * i
                            if 0 <= r < self.board_size and 0 <= c < self.board_size and self._current_moves[r][c].label == opponent_label:
                                count += 1
                            else:
                                break
                        if count < 3:
                            r, c = row - dr * count, col - dc * count
                            if 0 <= r < self.board_size and 0 <= c < self.board_size and self._current_moves[r][c].label == "":
                                open_ends += 1
                        if count == 2 and open_ends == 2:  # Chặn hàng 2 ô mở ở trung tâm
                            self._current_moves[row][col] = Move(row, col)
                            return Move(row, col, ai_label)
                    self._current_moves[row][col] = Move(row, col)
        return None

    def check_winning_move(self, ai_label):
        """Kiểm tra xem AI có thể thắng ngay lập tức hoặc tạo hàng 4/3 ô mở."""
        moves = self.get_nearby_moves()

        # Kiểm tra thắng ngay
        for row, col in moves:
            if self._current_moves[row][col].label == "":
                self._current_moves[row][col] = Move(row, col, ai_label)
                if self._check_winner(row, col):
                    self._current_moves[row][col] = Move(row, col)
                    return Move(row, col, ai_label)
                self._current_moves[row][col] = Move(row, col)

        # Kiểm tra tạo hàng 4 ô mở
        for row, col in moves:
            if self._current_moves[row][col].label == "":
                self._current_moves[row][col] = Move(row, col, ai_label)
                directions = [(0, 1), (1, 0), (1, 1), (1, -1)]
                for dr, dc in directions:
                    count = 1
                    open_ends = 0
                    for i in range(1, WIN_LENGTH):
                        r, c = row + dr * i, col + dc * i
                        if 0 <= r < self.board_size and 0 <= c < self.board_size and self._current_moves[r][c].label == ai_label:
                            count += 1
                        else:
                            break
                    if count < WIN_LENGTH:
                        r, c = row + dr * count, col + dc * count
                        if 0 <= r < self.board_size and 0 <= c < self.board_size and self._current_moves[r][c].label == "":
                            open_ends += 1
                    for i in range(1, WIN_LENGTH):
                        r, c = row - dr * i, col - dc * i
                        if 0 <= r < self.board_size and 0 <= c < self.board_size and self._current_moves[r][c].label == ai_label:
                            count += 1
                        else:
                            break
                    if count < WIN_LENGTH:
                        r, c = row - dr * count, col - dc * count
                        if 0 <= r < self.board_size and 0 <= c < self.board_size and self._current_moves[r][c].label == "":
                            open_ends += 1
                    if count == 4 and open_ends > 0:
                        self._current_moves[row][col] = Move(row, col)
                        return Move(row, col, ai_label)
                self._current_moves[row][col] = Move(row, col)

        # Kiểm tra tạo hàng 3 ô mở
        for row, col in moves:
            if self._current_moves[row][col].label == "":
                self._current_moves[row][col] = Move(row, col, ai_label)
                directions = [(0, 1), (1, 0), (1, 1), (1, -1)]
                for dr, dc in directions:
                    count = 1
                    open_ends = 0
                    for i in range(1, WIN_LENGTH - 1):
                        r, c = row + dr * i, col + dc * i
                        if 0 <= r < self.board_size and 0 <= c < self.board_size and self._current_moves[r][c].label == ai_label:
                            count += 1
                        else:
                            break
                    if count < 4:
                        r, c = row + dr * count, col + dc * count
                        if 0 <= r < self.board_size and 0 <= c < self.board_size and self._current_moves[r][c].label == "":
                            open_ends += 1
                    for i in range(1, WIN_LENGTH - 1):
                        r, c = row - dr * i, col - dc * i
                        if 0 <= r < self.board_size and 0 <= c < self.board_size and self._current_moves[r][c].label == ai_label:
                            count += 1
                        else:
                            break
                    if count < 4:
                        r, c = row - dr * count, col - dc * count
                        if 0 <= r < self.board_size and 0 <= c < self.board_size and self._current_moves[r][c].label == "":
                            open_ends += 1
                    if count == 3 and open_ends > 0:
                        self._current_moves[row][col] = Move(row, col)
                        return Move(row, col, ai_label)
                self._current_moves[row][col] = Move(row, col)
        return None

    def minimax(self, depth, alpha, beta, is_maximizing, max_depth=4, ai_label=None):
        score = self.evaluate_board(ai_label)
        if depth >= max_depth or score in [-1000, 1000] or self.is_tied():
            return score - depth if is_maximizing else score + depth

        moves = self.get_nearby_moves()
        start_time = time.time()
        
        # Sắp xếp nước đi theo đánh giá nhanh
        move_scores = []
        for row, col in moves:
            if time.time() - start_time > 2:
                break
            self._current_moves[row][col] = Move(row, col, ai_label if is_maximizing else ("O" if ai_label == "X" else "X"))
            score = self.evaluate_board(ai_label)
            self._current_moves[row][col] = Move(row, col)
            move_scores.append((row, col, score))
        
        # Sắp xếp moves dựa trên score, giữ nguyên định dạng (row, col)
        move_scores.sort(key=lambda x: x[2], reverse=is_maximizing)
        sorted_moves = [(row, col) for row, col, _ in move_scores]

        if is_maximizing:
            best_score = float('-inf')
            for row, col in sorted_moves:
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
            for row, col in sorted_moves:
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
        # Ưu tiên kiểm tra nước đi thắng ngay lập tức hoặc tạo hàng mạnh
        winning_move = self.check_winning_move(ai_label)
        if winning_move:
            print(f"AI chọn nước đi tấn công: [{winning_move.row}, {winning_move.col}]")
            return winning_move

        # Kiểm tra và chặn nếu đối thủ sắp thắng
        blocking_move = self.check_opponent_winning_move(ai_label)
        if blocking_move:
            print(f"AI chọn nước đi chặn: [{blocking_move.row}, {blocking_move.col}]")
            return blocking_move

        # Nếu không có nước đi thắng hoặc chặn ngay, dùng minimax
        best_score = float('-inf')
        best_move = None
        alpha = float('-inf')
        beta = float('inf')
        
        moves = self.get_nearby_moves()
        if not moves:
            moves = [(r, c) for r in range(self.board_size) for c in range(self.board_size) 
                     if self._current_moves[r][c].label == ""]

        start_time = time.time()
        evaluated_moves = []
        for row, col in moves:
            if time.time() - start_time > 2:
                break
            self._current_moves[row][col] = Move(row, col, ai_label)
            score = self.evaluate_board(ai_label)
            self._current_moves[row][col] = Move(row, col)
            evaluated_moves.append((score, (row, col)))

        # Sắp xếp và chọn nước đi tốt nhất trong thời gian cho phép
        evaluated_moves.sort(reverse=True)
        for score, (row, col) in evaluated_moves:
            if time.time() - start_time > 2:
                break
            self._current_moves[row][col] = Move(row, col, ai_label)
            move_score = self.minimax(0, alpha, beta, False, ai_label=ai_label)
            self._current_moves[row][col] = Move(row, col)
            if move_score > best_score:
                best_score = move_score
                best_move = Move(row, col, ai_label)
            alpha = max(alpha, best_score)

        if best_move:
            print(f"AI chọn nước đi: [{best_move.row}, {best_move.col}]")
            return best_move
        elif evaluated_moves:
            import random
            _, (row, col) = random.choice(evaluated_moves)
            print(f"AI chọn nước đi ngẫu nhiên do hết thời gian: [{row}, {col}]")
            return Move(row, col, ai_label)
        print("No valid moves available!")
        return None

if __name__ == "__main__":
    game = CaroGame()
    print(game.get_board_state())