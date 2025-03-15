# model/network.py
import socket
import threading
import json
import queue

class NetworkManager:
    def __init__(self):
        self.sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        self.conn = None
        self.is_host = False
        self.move_queue = queue.Queue()
        self.running = False
        self.connected_callback = None

    def host(self, port=5555, callback=None):
        """Khởi động server cho host trong thread riêng."""
        self.is_host = True
        self.connected_callback = callback
        self.sock.bind(('0.0.0.0', port))
        self.sock.listen(1)
        print("Waiting for a connection...")
        threading.Thread(target=self._accept_connection, daemon=True).start()

    def _accept_connection(self):
        """Chấp nhận kết nối trong thread riêng."""
        self.conn, addr = self.sock.accept()
        print(f"Connected to {addr}")
        self.running = True
        if self.connected_callback:
            self.connected_callback()  # Gọi callback khi kết nối thành công
        threading.Thread(target=self._listen, daemon=True).start()

    def join(self, host_ip, port=5555):
        """Kết nối đến host với tư cách client."""
        self.sock.connect((host_ip, port))
        self.conn = self.sock
        print(f"Connected to {host_ip}:{port}")
        self.running = True
        threading.Thread(target=self._listen, daemon=True).start()

    def send_move(self, row, col):
        """Gửi nước đi đến đối thủ."""
        if self.conn:
            move = {"row": row, "col": col}
            self.conn.send(json.dumps(move).encode())

    def get_move(self):
        """Lấy nước đi từ queue (không chặn)."""
        try:
            return self.move_queue.get_nowait()
        except queue.Empty:
            return None

    def _listen(self):
        """Lắng nghe nước đi từ đối thủ."""
        while self.running:
            try:
                data = self.conn.recv(1024).decode()
                if data:
                    move = json.loads(data)
                    self.move_queue.put((move["row"], move["col"]))
            except Exception as e:
                print(f"Network error: {e}")
                self.running = False
                break

    def close(self):
        """Đóng kết nối mạng."""
        self.running = False
        if self.conn:
            self.conn.close()
        self.sock.close()