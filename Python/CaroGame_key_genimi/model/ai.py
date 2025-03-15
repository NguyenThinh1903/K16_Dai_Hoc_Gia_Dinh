import google.generativeai as genai
from model.game import Move, CaroGame
import time

class CaroAI:
    def __init__(self, game: CaroGame):
        self.game = game
        self.board_size = game.board_size
        self.api_key = "Your Key"  # Đã thay bằng API key hợp lệ
        genai.configure(api_key=self.api_key)
        self.model = genai.GenerativeModel('gemini-1.5-flash')

    def get_ai_move(self, ai_label):
        board_state = self.game.get_board_state()
        nearby_moves = self.game.get_nearby_moves()
        opponent_label = "O" if ai_label == "X" else "X"

        if not any(cell != "" for row in board_state for cell in row):
            center = self.board_size // 2
            print(f"AI chọn vị trí trung tâm: [{center}, {center}]")
            return Move(center, center, ai_label)

        # Bước 1: Kiểm tra logic cơ bản trước khi gọi Gemini
        # Kiểm tra nước thắng ngay lập tức cho AI
        for r in range(self.board_size):
            for c in range(self.board_size):
                if board_state[r][c] == "":
                    temp_board = [row[:] for row in board_state]
                    temp_board[r][c] = ai_label
                    temp_game = CaroGame()
                    temp_game._current_moves = [[Move(i, j, temp_board[i][j]) for j in range(self.board_size)] for i in range(self.board_size)]
                    if temp_game._check_winner(r, c):
                        print(f"AI tìm thấy nước thắng ngay: [{r}, {c}]")
                        return Move(r, c, ai_label)

        # Kiểm tra và chặn ngay nếu đối thủ có 4 liên tiếp hoặc 3 liên tiếp với 2 đầu mở
        for r in range(self.board_size):
            for c in range(self.board_size):
                if board_state[r][c] == "":
                    temp_board = [row[:] for row in board_state]
                    temp_board[r][c] = opponent_label
                    temp_game = CaroGame()
                    temp_game._current_moves = [[Move(i, j, temp_board[i][j]) for j in range(self.board_size)] for i in range(self.board_size)]
                    # Kiểm tra chuỗi 4 (thắng ngay)
                    if temp_game._check_winner(r, c):
                        print(f"AI chặn đối thủ thắng ngay: [{r}, {c}]")
                        return Move(r, c, ai_label)
                    # Kiểm tra chuỗi 3 với 2 đầu mở
                    directions = [(0, 1), (1, 0), (1, 1), (1, -1)]
                    for dr, dc in directions:
                        count = 1
                        open_ends = 0
                        # Kiểm tra hướng dương
                        for i in range(1, 4):
                            rr, cc = r + dr * i, c + dc * i
                            if 0 <= rr < self.board_size and 0 <= cc < self.board_size and temp_board[rr][cc] == opponent_label:
                                count += 1
                            else:
                                break
                        if count < 4:
                            rr, cc = r + dr * count, c + dc * count
                            if 0 <= rr < self.board_size and 0 <= cc < self.board_size and temp_board[rr][cc] == "":
                                open_ends += 1
                        # Kiểm tra hướng âm
                        for i in range(1, 4):
                            rr, cc = r - dr * i, c - dc * i
                            if 0 <= rr < self.board_size and 0 <= cc < self.board_size and temp_board[rr][cc] == opponent_label:
                                count += 1
                            else:
                                break
                        if count < 4:
                            rr, cc = r - dr * count, c - dc * count
                            if 0 <= rr < self.board_size and 0 <= cc < self.board_size and temp_board[rr][cc] == "":
                                open_ends += 1
                        if count == 3 and open_ends == 2:
                            print(f"AI chặn chuỗi 3 với 2 đầu mở của đối thủ: [{r}, {c}]")
                            return Move(r, c, ai_label)

        # Bước 2: Gọi Gemini với prompt cải tiến
        prompt = (
            f"Đây là bàn cờ Caro 15x15 (mục tiêu: nối 5 ký tự liên tiếp theo hàng, cột, hoặc đường chéo để thắng):\n"
            f"{self._board_to_string(board_state)}\n"
            f"- Ký tự: 'X' và 'O'. Tôi là '{ai_label}', đối thủ là '{opponent_label}'.\n"
            f"- Quy tắc: Chỉ được đánh vào ô trống (dấu cách ' '). Hàng và cột bắt đầu từ 0.\n"
            f"- Nhiệm vụ: Quét toàn bộ bàn cờ và chọn nước đi tối ưu dựa trên chiến lược sau:\n"
            f"  1. Phân tích sâu: Mô phỏng 5 nước đi phía trước cho cả tôi và đối thủ để tìm nước đi tốt nhất.\n"
            f"  2. Phòng thủ:\n"
            f"     - Nếu đối thủ có 4 '{opponent_label}' liên tiếp với ít nhất 1 ô trống ở 1 đầu (ví dụ: '    {opponent_label}{opponent_label}{opponent_label}{opponent_label} ' ở hàng 7, cột 6-9), đánh vào ô trống để chặn (ở đây là [7, 5]).\n"
            f"     - Nếu đối thủ có 3 '{opponent_label}' liên tiếp với 2 ô trống ở 2 đầu (ví dụ: '  {opponent_label}{opponent_label}{opponent_label}  ' ở hàng 7, cột 6-8), đánh vào 1 trong 2 ô trống để chặn (ví dụ: [7, 5] hoặc [7, 9]).\n"
            f"     - Nếu đối thủ có 3 '{opponent_label}' liên tiếp với 1 ô trống ở 1 đầu (ví dụ: '  {opponent_label}{opponent_label}{opponent_label}X '), đánh vào ô trống để chặn (ở đây là [7, 5]).\n"
            f"  3. Tấn công:\n"
            f"     - Nếu tôi có 4 '{ai_label}' liên tiếp với ít nhất 1 ô trống ở 1 đầu, đánh vào ô trống để thắng ngay.\n"
            f"     - Tạo bẫy: Tìm nước đi để tạo 2 chuỗi 3 '{ai_label}' song song cách nhau 1 ô (ví dụ: '  {ai_label}{ai_label}{ai_label}  ' ở hàng 6, cột 6-8 và hàng 8, cột 6-8), buộc đối thủ phải chặn cả 2.\n"
            f"     - Nếu không có bẫy, tạo chuỗi 4 '{ai_label}' với ít nhất 1 ô trống ở 1 đầu.\n"
            f"     - Nếu không có chuỗi 4, tạo chuỗi 3 '{ai_label}' với 2 ô trống ở 2 đầu, ưu tiên gần trung tâm (gần [7, 7]).\n"
            f"  4. Phát triển: Nếu không có nước phòng thủ hoặc tấn công, đánh vào ô trống gần nhất với các ô đã có '{ai_label}' để mở rộng chuỗi, ưu tiên gần trung tâm.\n"
            f"  5. Đánh giá toàn cục: Kiểm tra từng hàng, cột, đường chéo chính và phụ để tìm tất cả chuỗi 4, 3, hoặc 2 liên tiếp của cả tôi và đối thủ. Ưu tiên nước đi theo thứ tự: chặn đối thủ thắng > tấn công để thắng > chặn chuỗi 3 của đối thủ > tạo bẫy > tạo chuỗi 4 > tạo chuỗi 3 > mở rộng.\n"
            f"  6. Nếu có nhiều lựa chọn cùng mức độ ưu tiên, chọn nước đi gần trung tâm nhất (gần [7, 7]).\n"
            f"  7. Không bao giờ chọn ngẫu nhiên. Phải phân tích từng bước trên để tìm nước đi tối ưu.\n"
            f"- Trả về nước đi dưới dạng [row, col], ví dụ: [7, 8]. Chỉ trả tọa độ, không thêm text khác.\n"
        )

        try:
            start_time = time.time()
            response = self.model.generate_content(prompt)
            move_str = response.text.strip()
            print(f"Gemini trả về: {move_str} (mất {time.time() - start_time:.2f}s)")
            row, col = self._parse_move(move_str)
            if 0 <= row < self.board_size and 0 <= col < self.board_size and board_state[row][col] == "":
                print(f"AI chọn nước đi: [{row}, {col}]")
                return Move(row, col, ai_label)
            else:
                print(f"Nước đi không hợp lệ: [{row}, {col}]")
        except Exception as e:
            print(f"Lỗi khi gọi Gemini API: {e}")

        if nearby_moves:
            row, col = nearby_moves[0]
            print(f"Dự phòng: Chọn nước đi gần nhất [{row}, {col}]")
            return Move(row, col, ai_label)
        return None

    def suggest_move(self, player_label):
        board_state = self.game.get_board_state()
        opponent_label = "O" if player_label == "X" else "X"

        prompt = (
            f"Đây là bàn cờ Caro 15x15 (mục tiêu: nối 5 ký tự liên tiếp theo hàng, cột, hoặc đường chéo để thắng):\n"
            f"{self._board_to_string(board_state)}\n"
            f"- Ký tự: 'X' và 'O'. Tôi là '{player_label}', đối thủ là '{opponent_label}'.\n"
            f"- Quy tắc: Chỉ được đánh vào ô trống (dấu cách ' '). Hàng và cột bắt đầu từ 0.\n"
            f"- Nhiệm vụ: Quét toàn bộ bàn cờ và chọn nước đi tối ưu dựa trên chiến lược sau:\n"
            f"  1. Phân tích sâu: Mô phỏng 5 nước đi phía trước cho cả tôi và đối thủ để tìm nước đi tốt nhất.\n"
            f"  2. Tấn công:\n"
            f"     - Nếu tôi có 4 '{player_label}' liên tiếp với ít nhất 1 ô trống ở 1 đầu (ví dụ: '    {player_label}{player_label}{player_label}{player_label} ' ở hàng 7, cột 6-9), đánh vào ô trống để thắng (ở đây là [7, 5]).\n"
            f"     - Tạo bẫy: Tìm nước đi để tạo 2 chuỗi 3 '{player_label}' song song cách nhau 1 ô (ví dụ: '  {player_label}{player_label}{player_label}  ' ở hàng 6, cột 6-8 và hàng 8, cột 6-8), buộc đối thủ phải chặn cả 2.\n"
            f"     - Nếu không có bẫy, tạo chuỗi 4 '{player_label}' với ít nhất 1 ô trống ở 1 đầu.\n"
            f"     - Nếu không có chuỗi 4, tạo chuỗi 3 '{player_label}' với 2 ô trống ở 2 đầu, ưu tiên gần trung tâm (gần [7, 7]).\n"
            f"  3. Phòng thủ:\n"
            f"     - Nếu đối thủ có 4 '{opponent_label}' liên tiếp với ít nhất 1 ô trống ở 1 đầu (ví dụ: '    {opponent_label}{opponent_label}{opponent_label}{opponent_label} ' ở hàng 7, cột 6-9), đánh vào ô trống để chặn (ở đây là [7, 5]).\n"
            f"     - Nếu đối thủ có 3 '{opponent_label}' liên tiếp với 2 ô trống ở 2 đầu (ví dụ: '  {opponent_label}{opponent_label}{opponent_label}  ' ở hàng 7, cột 6-8), đánh vào 1 trong 2 ô trống để chặn (ví dụ: [7, 5] hoặc [7, 9]).\n"
            f"     - Nếu đối thủ có 3 '{opponent_label}' liên tiếp với 1 ô trống ở 1 đầu (ví dụ: '  {opponent_label}{opponent_label}{opponent_label}X '), đánh vào ô trống để chặn (ở đây là [7, 5]).\n"
            f"  4. Phát triển: Nếu không có nước tấn công hoặc phòng thủ, đánh vào ô trống gần nhất với các ô đã có '{player_label}' để mở rộng chuỗi, ưu tiên gần trung tâm.\n"
            f"  5. Đánh giá toàn cục: Kiểm tra từng hàng, cột, đường chéo chính và phụ để tìm tất cả chuỗi 4, 3, hoặc 2 liên tiếp của cả tôi và đối thủ. Ưu tiên nước đi theo thứ tự: tấn công để thắng > chặn đối thủ thắng > tạo bẫy > tạo chuỗi 4 > chặn chuỗi 3 của đối thủ > tạo chuỗi 3 > mở rộng.\n"
            f"  6. Nếu có nhiều lựa chọn cùng mức độ ưu tiên, chọn nước đi gần trung tâm nhất (gần [7, 7]).\n"
            f"  7. Không bao giờ chọn ngẫu nhiên. Phải phân tích từng bước trên để tìm nước đi tối ưu.\n"
            f"- Trả về nước đi dưới dạng [row, col], ví dụ: [7, 8]. Chỉ trả tọa độ, không thêm text khác.\n"
        )

        try:
            response = self.model.generate_content(prompt)
            move_str = response.text.strip()
            print(f"Gemini gợi ý: {move_str}")
            row, col = self._parse_move(move_str)
            if 0 <= row < self.board_size and 0 <= col < self.board_size and board_state[row][col] == "":
                return Move(row, col, player_label)
        except Exception as e:
            print(f"Lỗi khi gợi ý với Gemini: {e}")

        moves = self.game.get_nearby_moves()
        if moves:
            row, col = moves[0]
            print(f"Dự phòng gợi ý: [{row}, {col}]")
            return Move(row, col, player_label)
        return None

    def _board_to_string(self, board_state):
        return "\n".join(" ".join(row) for row in board_state)

    def _parse_move(self, move_str):
        try:
            move = move_str.strip("[]").split(",")
            return int(move[0]), int(move[1])
        except (ValueError, IndexError):
            raise ValueError(f"Phản hồi không hợp lệ: {move_str}")