import time
from game import Move, CaroGame
import copy
import math

class CaroAI:
    def __init__(self, game: CaroGame):
        self.game = game
        self.board_size = game.board_size
        self.max_depth = 6
        self.time_limit = 2

    def evaluate_board(self, ai_label, board_state):
        if self.game.has_winner():
            return 10000 if self.game.current_player.label == ai_label else -10000
        if self.game.is_tied():
            return 0
        score = 0
        opponent_label = "O" if ai_label == "X" else "X"

        move_count = sum(1 for row in board_state for cell in row if cell != "")
        
        for row in range(self.board_size):
            for col in range(self.board_size):
                if board_state[row][col] != "":
                    label = board_state[row][col]
                    for dr, dc in [(0, 1), (1, 0), (1, 1), (1, -1)]:
                        count = 1
                        open_ends = 0
                        for i in range(1, 5):
                            r, c = row + dr * i, col + dc * i
                            if 0 <= r < self.board_size and 0 <= c < self.board_size and board_state[r][c] == label:
                                count += 1
                            else:
                                break
                        if count < 5:
                            r, c = row + dr * count, col + dc * count
                            if 0 <= r < self.board_size and 0 <= c < self.board_size and board_state[r][c] == "":
                                open_ends += 1
                        for i in range(1, 5):
                            r, c = row - dr * i, col - dc * i
                            if 0 <= r < self.board_size and 0 <= c < self.board_size and board_state[r][c] == label:
                                count += 1
                            else:
                                break
                        if count < 5:
                            r, c = row - dr * count, col - dc * count
                            if 0 <= r < self.board_size and 0 <= c < self.board_size and board_state[r][c] == "":
                                open_ends += 1
                        weight = count * count * 10
                        if count == 2:
                            weight *= 20
                        elif count == 3:
                            weight *= 200
                        elif count == 4:
                            weight *= 2000
                        if open_ends > 0:
                            weight *= 5
                        if label == ai_label:
                            score += weight
                            if count >= 4 and open_ends > 0:
                                score += 10000
                            elif count == 3 and open_ends == 2:
                                score += 5000
                            elif count == 3 and open_ends == 1:
                                score += 2000
                            elif count == 2 and open_ends == 2:
                                score += 1000
                            if move_count <= 2:
                                for dr2 in range(-1, 2):
                                    for dc2 in range(-1, 2):
                                        r2, c2 = row + dr2, col + dc2
                                        if (0 <= r2 < self.board_size and 0 <= c2 < self.board_size and
                                            board_state[r2][c2] == opponent_label):
                                            score += 300
                            center_distance = math.sqrt((row - self.board_size//2)**2 + (col - self.board_size//2)**2)
                            score += int(100 / (center_distance + 1))
                        else:
                            score -= weight * 2
                            if count >= 4 and open_ends > 0:
                                score -= 15000
                            elif count == 3 and open_ends == 2:
                                score -= 6000
                            elif count == 3 and open_ends == 1:
                                score -= 2500
                            elif count == 2 and open_ends == 2:
                                score -= 1200
        return score

    def check_opponent_winning_move(self, ai_label, board_state):
        opponent_label = "O" if ai_label == "X" else "X"
        moves = self.game.get_nearby_moves()

        for row, col in moves:
            if board_state[row][col] == "":
                temp_board = copy.deepcopy(board_state)
                temp_board[row][col] = opponent_label
                temp_game = CaroGame()
                temp_game._current_moves = [[Move(r, c, temp_board[r][c]) for c in range(self.board_size)] for r in range(self.board_size)]
                if temp_game._check_winner(row, col):
                    return Move(row, col, ai_label)

        for row, col in moves:
            if board_state[row][col] == "":
                temp_board = copy.deepcopy(board_state)
                temp_board[row][col] = opponent_label
                temp_game = CaroGame()
                temp_game._current_moves = [[Move(r, c, temp_board[r][c]) for c in range(self.board_size)] for r in range(self.board_size)]
                directions = [(0, 1), (1, 0), (1, 1), (1, -1)]
                for dr, dc in directions:
                    count = 1
                    open_ends = 0
                    for i in range(1, 5):
                        r, c = row + dr * i, col + dc * i
                        if 0 <= r < self.board_size and 0 <= c < self.board_size and temp_board[r][c] == opponent_label:
                            count += 1
                        else:
                            break
                    if count < 5:
                        r, c = row + dr * count, col + dc * count
                        if 0 <= r < self.board_size and 0 <= c < self.board_size and temp_board[r][c] == "":
                            open_ends += 1
                    for i in range(1, 5):
                        r, c = row - dr * i, col - dc * i
                        if 0 <= r < self.board_size and 0 <= c < self.board_size and temp_board[r][c] == opponent_label:
                            count += 1
                        else:
                            break
                    if count < 5:
                        r, c = row - dr * count, col - dc * count
                        if 0 <= r < self.board_size and 0 <= c < self.board_size and temp_board[r][c] == "":
                            open_ends += 1
                    if count == 4 and open_ends > 0:
                        return Move(row, col, ai_label)

        for row, col in moves:
            if board_state[row][col] == "":
                temp_board = copy.deepcopy(board_state)
                temp_board[row][col] = opponent_label
                temp_game = CaroGame()
                temp_game._current_moves = [[Move(r, c, temp_board[r][c]) for c in range(self.board_size)] for r in range(self.board_size)]
                directions = [(0, 1), (1, 0), (1, 1), (1, -1)]
                for dr, dc in directions:
                    count = 1
                    open_ends = 0
                    for i in range(1, 4):
                        r, c = row + dr * i, col + dc * i
                        if 0 <= r < self.board_size and 0 <= c < self.board_size and temp_board[r][c] == opponent_label:
                            count += 1
                        else:
                            break
                    if count < 4:
                        r, c = row + dr * count, col + dc * count
                        if 0 <= r < self.board_size and 0 <= c < self.board_size and temp_board[r][c] == "":
                            open_ends += 1
                    for i in range(1, 4):
                        r, c = row - dr * i, col - dc * i
                        if 0 <= r < self.board_size and 0 <= c < self.board_size and temp_board[r][c] == opponent_label:
                            count += 1
                        else:
                            break
                    if count < 4:
                        r, c = row - dr * count, col - dc * count
                        if 0 <= r < self.board_size and 0 <= c < self.board_size and temp_board[r][c] == "":
                            open_ends += 1
                    if count == 3 and open_ends > 0:
                        r_next, c_next = row + dr * (count if open_ends == 1 else -count), col + dc * (count if open_ends == 1 else -count)
                        if (0 <= r_next < self.board_size and 0 <= c_next < self.board_size and
                            temp_board[r_next][c_next] == ""):
                            return Move(row, col, ai_label)

        for row, col in moves:
            if board_state[row][col] == "":
                temp_board = copy.deepcopy(board_state)
                temp_board[row][col] = opponent_label
                temp_game = CaroGame()
                temp_game._current_moves = [[Move(r, c, temp_board[r][c]) for c in range(self.board_size)] for r in range(self.board_size)]
                directions = [(0, 1), (1, 0), (1, 1), (1, -1)]
                for dr, dc in directions:
                    count = 1
                    open_ends = 0
                    for i in range(1, 3):
                        r, c = row + dr * i, col + dc * i
                        if 0 <= r < self.board_size and 0 <= c < self.board_size and temp_board[r][c] == opponent_label:
                            count += 1
                        else:
                            break
                    if count < 3:
                        r, c = row + dr * count, col + dc * count
                        if 0 <= r < self.board_size and 0 <= c < self.board_size and temp_board[r][c] == "":
                            open_ends += 1
                    for i in range(1, 3):
                        r, c = row - dr * i, col - dc * i
                        if 0 <= r < self.board_size and 0 <= c < self.board_size and temp_board[r][c] == opponent_label:
                            count += 1
                        else:
                            break
                    if count < 3:
                        r, c = row - dr * count, col - dc * count
                        if 0 <= r < self.board_size and 0 <= c < self.board_size and temp_board[r][c] == "":
                            open_ends += 1
                    if count == 2 and open_ends == 2:
                        return Move(row, col, ai_label)
        return None

    def check_winning_move(self, ai_label, board_state):
        moves = self.game.get_nearby_moves()
        for row, col in moves:
            if board_state[row][col] == "":
                temp_board = copy.deepcopy(board_state)
                temp_board[row][col] = ai_label
                temp_game = CaroGame()
                temp_game._current_moves = [[Move(r, c, temp_board[r][c]) for c in range(self.board_size)] for r in range(self.board_size)]
                if temp_game._check_winner(row, col):
                    return Move(row, col, ai_label)
        return None

    def minimax(self, depth, alpha, beta, is_maximizing, ai_label, board_state):
        score = self.evaluate_board(ai_label, board_state)
        if depth >= self.max_depth or abs(score) >= 10000 or self.game.is_tied():
            return score - depth if is_maximizing else score + depth

        moves = self.game.get_nearby_moves()
        start_time = time.time()
        
        move_scores = []
        for row, col in moves:
            if time.time() - start_time > self.time_limit:
                break
            temp_board = copy.deepcopy(board_state)
            temp_board[row][col] = ai_label if is_maximizing else ("O" if ai_label == "X" else "X")
            score = self.evaluate_board(ai_label, temp_board)
            center_distance = math.sqrt((row - self.board_size//2)**2 + (col - self.board_size//2)**2)
            score += int(100 / (center_distance + 1))
            move_scores.append((row, col, score))
        
        move_scores.sort(key=lambda x: x[2], reverse=is_maximizing)
        sorted_moves = [(row, col) for row, col, _ in move_scores]

        if is_maximizing:
            best_score = float('-inf')
            for row, col in sorted_moves:
                if time.time() - start_time > self.time_limit:
                    break
                temp_board = copy.deepcopy(board_state)
                temp_board[row][col] = ai_label
                score = self.minimax(depth + 1, alpha, beta, False, ai_label, temp_board)
                best_score = max(best_score, score)
                alpha = max(alpha, best_score)
                if beta <= alpha:
                    break
            return best_score
        else:
            best_score = float('inf')
            for row, col in sorted_moves:
                if time.time() - start_time > self.time_limit:
                    break
                opponent_label = "O" if ai_label == "X" else "X"
                temp_board = copy.deepcopy(board_state)
                temp_board[row][col] = opponent_label
                score = self.minimax(depth + 1, alpha, beta, True, ai_label, temp_board)
                best_score = min(best_score, score)
                beta = min(beta, best_score)
                if beta <= alpha:
                    break
            return best_score

    def get_ai_move(self, ai_label):
        board_state = self.game.get_board_state()
        winning_move = self.check_winning_move(ai_label, board_state)
        if winning_move:
            print(f"AI chọn nước đi tấn công: [{winning_move.row}, {winning_move.col}]")
            return winning_move

        blocking_move = self.check_opponent_winning_move(ai_label, board_state)
        if blocking_move:
            print(f"AI chọn nước đi chặn: [{blocking_move.row}, {blocking_move.col}]")
            return blocking_move

        best_score = float('-inf')
        best_move = None
        alpha = float('-inf')
        beta = float('inf')
        
        moves = self.game.get_nearby_moves()
        if not moves:
            moves = [(r, c) for r in range(self.board_size) for c in range(self.board_size) 
                     if board_state[r][c] == ""]
            # Nếu bàn cờ trống, chọn vị trí trung tâm
            if not any(cell != "" for row in board_state for cell in row):
                center = self.board_size // 2
                print(f"AI chọn vị trí trung tâm: [{center}, {center}]")
                return Move(center, center, ai_label)

        start_time = time.time()
        evaluated_moves = []
        for row, col in moves:
            if time.time() - start_time > self.time_limit:
                print(f"AI hết thời gian sau {self.time_limit} giây, chọn nước đi tốt nhất hiện tại.")
                break
            temp_board = copy.deepcopy(board_state)
            temp_board[row][col] = ai_label
            score = self.evaluate_board(ai_label, temp_board)
            center_distance = math.sqrt((row - self.board_size//2)**2 + (col - self.board_size//2)**2)
            score += int(100 / (center_distance + 1))
            evaluated_moves.append((score, (row, col)))

        evaluated_moves.sort(reverse=True)
        for score, (row, col) in evaluated_moves:
            if time.time() - start_time > self.time_limit:
                print(f"AI hết thời gian, chọn nước đi tốt nhất hiện tại: [{row}, {col}]")
                return Move(row, col, ai_label)
            temp_board = copy.deepcopy(board_state)
            temp_board[row][col] = ai_label
            move_score = self.minimax(0, alpha, beta, False, ai_label, temp_board)
            if move_score > best_score:
                best_score = move_score
                best_move = Move(row, col, ai_label)
            alpha = max(alpha, best_score)

        if best_move:
            print(f"AI chọn nước đi: [{best_move.row}, {best_move.col}]")
            return best_move
        elif evaluated_moves:
            _, (row, col) = evaluated_moves[0]  # Chọn nước đi tốt nhất nếu hết thời gian
            print(f"AI chọn nước đi tốt nhất hiện tại: [{row}, {col}]")
            return Move(row, col, ai_label)
        print("No valid moves available!")
        return None