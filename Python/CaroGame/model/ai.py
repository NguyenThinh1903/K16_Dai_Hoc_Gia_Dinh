import time
import math
from model.game import Move, CaroGame
import copy
from model.evaluator import BoardEvaluator

class CaroAI:
    def __init__(self, game: CaroGame):
        self.game = game
        self.board_size = game.board_size
        self.max_depth = 6  # Tăng độ sâu để dự đoán tốt hơn
        self.time_limit = 1.5  # Tăng thời gian để tính toán
        self.evaluator = BoardEvaluator(game)

    def minimax(self, depth, alpha, beta, is_maximizing, ai_label, board_state):
        score = self.evaluator.evaluate_board(ai_label, board_state)
        if depth >= self.max_depth or abs(score) >= 100000 or self.game.is_tied():
            return score - depth if is_maximizing else score + depth

        moves = self.game.get_nearby_moves()
        start_time = time.time()
        
        move_scores = []
        for row, col in moves:
            if time.time() - start_time > self.time_limit:
                break
            temp_board = copy.deepcopy(board_state)
            temp_board[row][col] = ai_label if is_maximizing else ("O" if ai_label == "X" else "X")
            score = self.evaluator.evaluate_board(ai_label, temp_board)
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
        winning_move = self.evaluator.check_winning_move(ai_label, board_state)
        if winning_move:
            print(f"AI chọn nước đi tấn công: [{winning_move.row}, {winning_move.col}]")
            return winning_move

        blocking_move = self.evaluator.check_opponent_winning_move(ai_label, board_state)
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
            score = self.evaluator.evaluate_board(ai_label, temp_board)
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

    def suggest_move(self, player_label):
        """
        Gợi ý nước đi cho người chơi dựa trên thuật toán minimax.
        Ưu tiên nước thắng hoặc chặn đối thủ trước khi dùng minimax.
        """
        board_state = self.game.get_board_state()
        best_score = float('-inf')
        best_move = None
        alpha = float('-inf')
        beta = float('inf')

        # Ưu tiên nước đi thắng
        winning_move = self.evaluator.check_winning_move(player_label, board_state)
        if winning_move:
            print(f"Gợi ý nước đi thắng cho người chơi: [{winning_move.row}, {winning_move.col}]")
            return winning_move

        # Ưu tiên chặn đối thủ
        blocking_move = self.evaluator.check_opponent_winning_move(player_label, board_state)
        if blocking_move:
            print(f"Gợi ý nước đi chặn đối thủ: [{blocking_move.row}, {blocking_move.col}]")
            return blocking_move

        moves = self.game.get_nearby_moves()
        if not moves:
            moves = [(r, c) for r in range(self.board_size) for c in range(self.board_size) 
                     if board_state[r][c] == ""]

        start_time = time.time()
        for row, col in moves:
            if time.time() - start_time > self.time_limit:
                print(f"Gợi ý hết thời gian sau {self.time_limit} giây, chọn nước đi tốt nhất hiện tại.")
                break
            if board_state[row][col] == "":
                temp_board = copy.deepcopy(board_state)
                temp_board[row][col] = player_label
                score = self.evaluator.evaluate_board(player_label, temp_board)
                if score > best_score:
                    best_score = score
                    best_move = Move(row, col, player_label)
                alpha = max(alpha, best_score)
                # Kiểm tra minimax để cải thiện
                move_score = self.minimax(0, alpha, beta, False, player_label, temp_board)
                if move_score > best_score:
                    best_score = move_score
                    best_move = Move(row, col, player_label)

        if best_move:
            print(f"Gợi ý nước đi cho người chơi: [{best_move.row}, {best_move.col}]")
            return best_move
        elif moves:
            row, col = moves[0]  # Chọn nước đi đầu tiên nếu không tìm thấy tối ưu
            print(f"Gợi ý nước đi mặc định: [{row}, {col}]")
            return Move(row, col, player_label)
        print("Không tìm thấy nước đi gợi ý!")
        return None