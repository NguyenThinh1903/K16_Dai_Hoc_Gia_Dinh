import time
import copy
import math
from model.game import Move, CaroGame
from model.evaluator import BoardEvaluator

class SuggestAI:
    def __init__(self, game: CaroGame):
        self.game = game
        self.board_size = game.board_size
        self.max_depth = 10  # Deeper than CaroAI's 6 for superior foresight
        self.time_limit = 3.0  # Longer than CaroAI's 1.5 for better computation
        self.evaluator = BoardEvaluator(game)

    def minimax(self, depth, alpha, beta, is_maximizing, player_label, board_state):
        """Minimax with alpha-beta pruning, optimized for traps and winning."""
        score = self._evaluate_board(player_label, board_state)
        if depth >= self.max_depth or abs(score) >= 100000 or self.game.is_tied():
            return score - depth if is_maximizing else score + depth

        moves = self.game.get_nearby_moves() or [
            (r, c) for r in range(self.board_size) for c in range(self.board_size) 
            if board_state[r][c] == ""
        ]
        start_time = time.time()

        # Pre-evaluate and sort moves
        move_scores = []
        for row, col in moves:
            if time.time() - start_time > self.time_limit:
                break
            temp_board = copy.deepcopy(board_state)
            temp_board[row][col] = player_label if is_maximizing else ("O" if player_label == "X" else "X")
            score = self._evaluate_board(player_label, temp_board)
            move_scores.append((score, row, col))
        move_scores.sort(key=lambda x: x[0], reverse=is_maximizing)
        sorted_moves = [(row, col) for _, row, col in move_scores]

        if is_maximizing:
            best_score = float('-inf')
            for row, col in sorted_moves:
                if time.time() - start_time > self.time_limit:
                    break
                temp_board = copy.deepcopy(board_state)
                temp_board[row][col] = player_label
                score = self.minimax(depth + 1, alpha, beta, False, player_label, temp_board)
                best_score = max(best_score, score)
                alpha = max(alpha, best_score)
                if beta <= alpha:
                    break
            return best_score
        else:
            best_score = float('inf')
            opponent_label = "O" if player_label == "X" else "X"
            for row, col in sorted_moves:
                if time.time() - start_time > self.time_limit:
                    break
                temp_board = copy.deepcopy(board_state)
                temp_board[row][col] = opponent_label
                score = self.minimax(depth + 1, alpha, beta, True, player_label, temp_board)
                best_score = min(best_score, score)
                beta = min(beta, best_score)
                if beta <= alpha:
                    break
            return best_score

    def _evaluate_board(self, player_label, board_state):
        """Custom evaluation prioritizing traps and winning sequences."""
        opponent_label = "O" if player_label == "X" else "X"
        score = 0
        directions = [(0, 1), (1, 0), (1, 1), (1, -1)]

        player_threes = 0  # Count of open 3s for trap potential
        for row in range(self.board_size):
            for col in range(self.board_size):
                if board_state[row][col] != "":
                    label = board_state[row][col]
                    for dr, dc in directions:
                        count = 1
                        open_ends = 0
                        # Forward count
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
                        # Backward count
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
                        # Scoring
                        if label == player_label:
                            if count >= 5:
                                return 100000  # Win
                            elif count == 4 and open_ends >= 1:
                                score += 20000  # Open 4 is nearly a win
                            elif count == 3 and open_ends >= 1:
                                player_threes += 1
                                score += 10000  # Open 3 is a strong threat
                            elif count == 2 and open_ends >= 1:
                                score += 2000   # Open 2 builds potential
                            score += count * 300 * open_ends  # Aggressive bonus
                        elif label == opponent_label:
                            if count >= 5:
                                return -100000  # Loss
                            elif count >= 4 and open_ends >= 1:
                                score -= 15000  # Critical opponent threat
                            elif count == 3 and open_ends >= 1:
                                score -= 5000   # Only penalize 3+
                            # Ignore opponent’s shorter sequences

        # Bonus for creating double threats (e.g., two open 3s)
        if player_threes >= 2:
            score += 50000  # Double 3s are often unstoppable

        return score

    def get_suggested_move(self, player_label):
        """Suggest a move to beat CaroAI by prioritizing wins and traps."""
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

        # 2. Block opponent’s 4+ only (let CaroAI waste moves on weaker blocks)
        directions = [(0, 1), (1, 0), (1, 1), (1, -1)]
        for row, col in moves:
            if board_state[row][col] != "":
                continue
            temp_board = copy.deepcopy(board_state)
            temp_board[row][col] = opponent_label
            for dr, dc in directions:
                count = 1
                open_ends = 0
                for i in range(1, 5):
                    r, c = row + dr * i, col + dc * i
                    if not (0 <= r < self.board_size and 0 <= c < self.board_size):
                        break
                    if temp_board[r][c] == opponent_label:
                        count += 1
                    elif temp_board[r][c] == "":
                        open_ends += 1
                        break
                    else:
                        break
                for i in range(1, 5):
                    r, c = row - dr * i, col - dc * i
                    if not (0 <= r < self.board_size and 0 <= c < self.board_size):
                        break
                    if temp_board[r][c] == opponent_label:
                        count += 1
                    elif temp_board[r][c] == "":
                        open_ends += 1
                        break
                    else:
                        break
                if count >= 4 and open_ends > 0:  # Block only 4+
                    print(f"Suggested block of opponent's 4+: [{row}, {col}]")
                    return Move(row, col, player_label)

        # 3. Start at center
        if not any(cell != "" for row in board_state for cell in row):
            center = self.board_size // 2
            print(f"Suggested center move: [{center}, {center}]")
            return Move(center, center, player_label)

        # 4. Optimal move with minimax, focusing on traps
        best_score = float('-inf')
        best_move = None
        alpha = float('-inf')
        beta = float('inf')

        # Pre-evaluate moves
        evaluated_moves = []
        for row, col in moves:
            if time.time() - start_time > self.time_limit:
                break
            temp_board = copy.deepcopy(board_state)
            temp_board[row][col] = player_label
            score = self._evaluate_board(player_label, temp_board)
            center_distance = math.sqrt((row - self.board_size//2)**2 + (col - self.board_size//2)**2)
            score += int(500 / (center_distance + 1))  # Stronger center bias
            evaluated_moves.append((score, Move(row, col, player_label)))

        evaluated_moves.sort(key=lambda x: x[0], reverse=True)
        top_moves = evaluated_moves[:min(7, len(evaluated_moves))]  # Top 7 moves for deeper analysis

        # Minimax on top moves
        for _, move in top_moves:
            if time.time() - start_time > self.time_limit:
                break
            temp_board = copy.deepcopy(board_state)
            temp_board[move.row][move.col] = player_label
            score = self.minimax(0, alpha, beta, False, player_label, temp_board)
            if score > best_score:
                best_score = score
                best_move = move
            alpha = max(alpha, best_score)

        if best_move:
            print(f"Suggested trap/attack move: [{best_move.row}, {best_move.col}]")
            return best_move
        elif evaluated_moves:
            _, move = evaluated_moves[0]
            print(f"Suggested best available move: [{move.row}, {move.col}]")
            return move

        print("No suggested move found!")
        return None