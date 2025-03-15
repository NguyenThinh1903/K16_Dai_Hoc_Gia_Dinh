# game_launcher.py
import socket
import tkinter as tk
import tkinter.messagebox as messagebox
import tkinter.simpledialog as simpledialog
from view.menu import StartMenu
from model.game import CaroGame
from controller.controller import CaroController
from controller.online_controller import OnlineCaroController
from view.board import CaroBoard

class GameLauncher:
    def __init__(self, menu):
        self.menu = menu
        self.setup_callbacks()

    def get_local_ip(self):
        """Lấy địa chỉ IP nội bộ của máy."""
        s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
        try:
            s.connect(('10.255.255.255', 1))
            ip = s.getsockname()[0]
        except Exception:
            ip = '127.0.0.1'
        finally:
            s.close()
        return ip

    def start_pvp(self):
        self.menu.withdraw()
        game = CaroGame()
        board = CaroBoard(None)
        controller = CaroController(game, board, self.menu)
        board._controller = controller
        controller.start()
        board.mainloop()

    def start_pvai(self):
        self.menu.withdraw()
        game = CaroGame()
        board = CaroBoard(None)
        controller = CaroController(game, board, self.menu)
        board._controller = controller
        controller.set_ai_mode()
        controller.start()
        board.mainloop()

    def start_host(self):
        local_ip = self.get_local_ip()
        messagebox.showinfo("Host Info", f"Your IP: {local_ip}\nShare this with your friend to join!", parent=self.menu)
        self.menu.withdraw()
        game = CaroGame()
        board = CaroBoard(None)
        controller = OnlineCaroController(game, board, self.menu)
        board._controller = controller
        controller.set_online_mode(is_host=True)
        board.mainloop()

    def start_join(self):
        host_ip = simpledialog.askstring("Input", "Enter host IP:", parent=self.menu)
        if host_ip:
            self.menu.withdraw()
            game = CaroGame()
            board = CaroBoard(None)
            controller = OnlineCaroController(game, board, self.menu)
            board._controller = controller
            controller.set_online_mode(is_host=False, host_ip=host_ip)
            board.mainloop()

    def setup_callbacks(self):
        self.menu.set_callbacks(self.start_pvp, self.start_pvai, self.start_host, self.start_join)

    def run(self):
        self.menu.mainloop()