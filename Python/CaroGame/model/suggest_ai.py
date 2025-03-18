import time
import math
from model.game import Move, CaroGame
from model.evaluator import BoardEvaluator

class SuggestAI:
    def __init__(self, game: CaroGame):
        self.game = game
        self.board_size = game.board_size
        self.max_depth = 10  # Giữ nguyên độ sâu mạnh mẽ
        self.time_limit = 3.0  # Giữ nguyên để đảm bảo chất lượng
        self.evaluator = BoardEvaluator(game)
        self.transposition_table = {}  # Thêm bảng chuyển vị

    def minimax(self, depth, alpha, beta, is_maximizing, player_label, board_state, start_time):
        """Minimax với alpha-beta pruning và bảng chuyển vị."""
        state_key = self._hash_board(board_state)
        if state_key in self.transposition_table and self.transposition_table[state_key][0] >= depth:
            return self.transposition_table[state_key][1]

        score = self._evaluate_board(player_label, board_state)
        if depth >= self.max_depth or abs(score) >= 100000 or self.game.is_tied():
            adjusted_score = score - depth if is_maximizing else score + depth
            self.transposition_table[state_key] = (depth, adjusted_score)
            return adjusted_score

        moves = self.game.get_nearby_moves() or [
            (r, c) for r in range(self.board_size) for c in range(self.board_size) 
            if board_state[r][c] == ""
        ]

        # Đánh giá trước và sắp xếp nước đi
        move_scores = self._pre_evaluate_moves(moves, player_label, board_state, is_maximizing)
        sorted_moves = [move for _, move in sorted(move_scores, key=lambda x: x[0], reverse=is_maximizing)]

        if is_maximizing:
            best_score = float('-inf')
            for row, col in sorted_moves:
                if time.time() - start_time > self.time_limit:
                    break
                board_state[row][col] = player_label
                score = self.minimax(depth + 1, alpha, beta, False, player_label, board_state, start_time)
                board_state[row][col] = ""  # Khôi phục thay vì deepcopy
                best_score = max(best_score, score)
                alpha = max(alpha, best_score)
                if beta <= alpha:
                    break
            self.transposition_table[state_key] = (depth, best_score)
            return best_score
        else:
            best_score = float('inf')
            opponent_label = "O" if player_label == "X" else "X"
            for row, col in sorted_moves:
                if time.time() - start_time > self.time_limit:
                    break
                board_state[row][col] = opponent_label
                score = self.minimax(depth + 1, alpha, beta, True, player_label, board_state, start_time)
                board_state[row][col] = ""
                best_score = min(best_score, score)
                beta = min(beta, best_score)
                if beta <= alpha:
                    break
            self.transposition_table[state_key] = (depth, best_score)
            return best_score

    def _hash_board(self, board_state):
        """Tạo mã băm đơn giản cho trạng thái bảng."""
        return ''.join(''.join(row) for row in board_state)

    def _pre_evaluate_moves(self, moves, player_label, board_state, is_maximizing):
        """Đánh giá trước các nước đi để sắp xếp."""
        move_scores = []
        opponent_label = "O" if player_label == "X" else "X"
        for row, col in moves:
            label = player_label if is_maximizing else opponent_label
            board_state[row][col] = label
            score = self._evaluate_board(player_label, board_state)
            board_state[row][col] = ""
            move_scores.append((score, (row, col)))
        return move_scores

    def _evaluate_board(self, player_label, board_state):
        """Đánh giá bảng với trọng số ưu tiên bẫy và thắng (giữ nguyên logic gốc)."""
        opponent_label = "O" if player_label == "X" else "X"
        score = 0
        directions = [(0, 1), (1, 0), (1, 1), (1, -1)]
        player_threes = 0

        for row in range(self.board_size):
            for col in range(self.board_size):
                if board_state[row][col] != "":
                    label = board_state[row][col]
                    for dr, dc in directions:
                        count = 1
                        open_ends = 0
                        for i in range(1, 5):
                            r, c = row + dr * i, col + dc * i
                            if not (0 <= r < self.board_size and 0 <= c < self.board_size):
                                break
                            if board_state[r][c] == label:
                                count += 1
                            elif board_state[r][c] == "":
                                open_ends += 1
                                break
                            else:
                                break
                        for i in range(1, 5):
                            r, c = row - dr * i, col - dc * i
                            if not (0 <= r < self.board_size and 0 <= c < self.board_size):
                                break
                            if board_state[r][c] == label:
                                count += 1
                            elif board_state[r][c] == "":
                                open_ends += 1
                                break
                            else:
                                break
                        if label == player_label:
                            if count >= 5:
                                return 100000
                            elif count == 4 and open_ends >= 1:
                                score += 20000
                            elif count == 3 and open_ends >= 1:
                                player_threes += 1
                                score += 10000
                            elif count == 2 and open_ends >= 1:
                                score += 2000
                            score += count * 300 * open_ends
                        elif label == opponent_label:
                            if count >= 5:
                                return -100000
                            elif count >= 4 and open_ends >= 1:
                                score -= 15000
                            elif count == 3 and open_ends >= 1:
                                score -= 5000

        if player_threes >= 2:
            score += 50000
        return score

    def get_suggested_move(self, player_label):
        """Tìm nước đi tối ưu với chiến lược bẫy và thắng nhanh (giữ logic gốc, tối ưu tốc độ)."""
        board_state = self.game.get_board_state()
        opponent_label = "O" if player_label == "X" else "X"
        moves = self.game.get_nearby_moves() or [
            (r, c) for r in range(self.board_size) for c in range(self.board_size) 
            if board_state[r][c] == ""
        ]
        start_time = time.time()

        # 1. Immediate win
        winning_move = self.evaluator.check_winning_move(player_label, board_state)
        if winning_move:
            print(f"Suggested winning move: [{winning_move.row}, {winning_move.col}]")
            return winning_move

        # 2. Block opponent’s 4+
        directions = [(0, 1), (1, 0), (1, 1), (1, -1)]
        for row, col in moves:
            if board_state[row][col] != "":
                continue
            board_state[row][col] = opponent_label
            for dr, dc in directions:
                count = 1
                open_ends = 0
                for i in range(1, 5):
                    r, c = row + dr * i, col + dc * i
                    if not (0 <= r < self.board_size and 0 <= c < self.board_size):
                        break
                    if board_state[r][c] == opponent_label:
                        count += 1
                    elif board_state[r][c] == "":
                        open_ends += 1
                        break
                    else:
                        break
                for i in range(1, 5):
                    r, c = row - dr * i, col - dc * i
                    if not (0 <= r < self.board_size and 0 <= c < self.board_size):
                        break
                    if board_state[r][c] == opponent_label:
                        count += 1
                    elif board_state[r][c] == "":
                        open_ends += 1
                        break
                    else:
                        break
                if count >= 4 and open_ends > 0:
                    board_state[row][col] = ""
                    print(f"Suggested block of opponent's 4+: [{row}, {col}]")
                    return Move(row, col, player_label)
            board_state[row][col] = ""

        # 3. Start at center
        if not any(cell != "" for row in board_state for cell in row):
            center = self.board_size // 2
            print(f"Suggested center move: [{center}, {center}]")
            return Move(center, center, player_label)

        # 4. Optimal move with minimax
        best_score = float('-inf')
        best_move = None
        alpha = float('-inf')
        beta = float('inf')

        # Lọc top 7 nước đi tiềm năng
        move_scores = self._pre_evaluate_moves(moves, player_label, board_state, True)
        top_moves = sorted(move_scores, key=lambda x: x[0], reverse=True)[:min(7, len(move_scores))]

        for _, (row, col) in top_moves:
            if time.time() - start_time > self.time_limit:
                break
            board_state[row][col] = player_label
            score = self.minimax(0, alpha, beta, False, player_label, board_state, start_time)
            board_state[row][col] = ""
            if score > best_score:
                best_score = score
                best_move = Move(row, col, player_label)
            alpha = max(alpha, best_score)

        if best_move:
            print(f"Suggested trap/attack move: [{best_move.row}, {best_move.col}]")
            return best_move
        elif move_scores:
            _, (row, col) = move_scores[0]
            print(f"Suggested best available move: [{row}, {col}]")
            return Move(row, col, player_label)

        print("No suggested move found!")
        return None