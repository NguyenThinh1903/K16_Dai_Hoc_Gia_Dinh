import copy
import math
from model.game import Move, CaroGame

class BoardEvaluator:
    def __init__(self, game: CaroGame):
        self.game = game
        self.board_size = game.board_size

    def evaluate_board(self, ai_label, board_state):
        if self.game.has_winner():
            return 100000 if self.game.current_player.label == ai_label else -100000  # Tăng giá trị chiến thắng
        if self.game.is_tied():
            return 0
        score = 0
        opponent_label = "O" if ai_label == "X" else "X"

        move_count = sum(1 for row in board_state for cell in row if cell != "")
        
        for row in range(self.board_size):
            for col in range(self.board_size):
                if board_state[row][col] != "":
                    label = board_state[row][col]
                    for dr, dc in [(0, 1), (1, 0), (1, 1), (1, -1)]:  # Kiểm tra 4 hướng
                        count = 1
                        open_ends = 0
                        # Kiểm tra theo hướng dương
                        for i in range(1, 5):
                            r, c = row + dr * i, col + dc * i
                            if (0 <= r < self.board_size and 0 <= c < self.board_size and 
                                board_state[r][c] == label):
                                count += 1
                            else:
                                break
                        if count < 5:
                            r, c = row + dr * count, col + dc * count
                            if (0 <= r < self.board_size and 0 <= c < self.board_size and 
                                board_state[r][c] == ""):
                                open_ends += 1
                        # Kiểm tra theo hướng âm
                        for i in range(1, 5):
                            r, c = row - dr * i, col - dc * i
                            if (0 <= r < self.board_size and 0 <= c < self.board_size and 
                                board_state[r][c] == label):
                                count += 1
                            else:
                                break
                        if count < 5:
                            r, c = row - dr * count, col - dc * count
                            if (0 <= r < self.board_size and 0 <= c < self.board_size and 
                                board_state[r][c] == ""):
                                open_ends += 1

                        # Đánh giá dựa trên số lượng liên tiếp và đầu mở
                        weight = count * count * 10
                        if count == 2:
                            weight *= 20
                        elif count == 3:
                            weight *= 500  # Tăng trọng số cho 3 liên tiếp
                        elif count == 4:
                            weight *= 5000  # Tăng trọng số cho 4 liên tiếp

                        if open_ends > 0:
                            weight *= 10  # Ưu tiên các nước có đầu mở
                        if label == ai_label:
                            score += weight
                            # Ưu tiên chiến thắng hoặc tạo cơ hội thắng
                            if count >= 4 and open_ends > 0:
                                score += 50000  # Ưu tiên cao nhất cho 4 liên tiếp với đầu mở
                            elif count == 3 and open_ends == 2:
                                score += 10000  # Ưu tiên cao cho 3 liên tiếp với 2 đầu mở
                            elif count == 3 and open_ends == 1:
                                score += 5000
                            elif count == 2 and open_ends == 2:
                                score += 2000
                            # Ưu tiên trung tâm
                            center_distance = math.sqrt((row - self.board_size//2)**2 + (col - self.board_size//2)**2)
                            score += int(100 / (center_distance + 1))
                        else:  # Đối thủ
                            score -= weight * 2  # Trừng phạt đối thủ
                            if count >= 4 and open_ends > 0:
                                score -= 75000  # Ưu tiên chặn 4 liên tiếp
                            elif count == 3 and open_ends == 2:
                                score -= 15000  # Ưu tiên chặn 3 liên tiếp với 2 đầu mở
                            elif count == 3 and open_ends == 1:
                                score -= 7000
                            elif count == 2 and open_ends == 2:
                                score -= 3000

        return score

    def check_opponent_winning_move(self, ai_label, board_state):
        opponent_label = "O" if ai_label == "X" else "X"
        moves = self.game.get_nearby_moves()

        # Ưu tiên chặn nước thắng của đối thủ
        for row, col in moves:
            if board_state[row][col] == "":
                temp_board = copy.deepcopy(board_state)
                temp_board[row][col] = opponent_label
                temp_game = CaroGame()
                temp_game._current_moves = [[Move(r, c, temp_board[r][c]) for c in range(self.board_size)] for r in range(self.board_size)]
                if temp_game._check_winner(row, col):
                    return Move(row, col, ai_label)

        # Ưu tiên chặn 4 liên tiếp
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

        # Ưu tiên chặn 3 liên tiếp với đầu mở
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