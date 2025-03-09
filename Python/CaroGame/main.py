import sys  # Thêm import sys để sử dụng sys.exit()
from view.menu import StartMenu
from model.game import CaroGame
from controller.controller import CaroController
from view.board import CaroBoard

def main():
    # Khởi tạo menu
    menu = StartMenu()
    
    # Định nghĩa các hàm khởi tạo game
    def start_pvp():
        menu.withdraw()
        game = CaroGame()
        board = CaroBoard(None)
        controller = CaroController(game, board, menu)
        board._controller = controller
        controller.start()

    def start_pvai():
        menu.withdraw()
        game = CaroGame()
        board = CaroBoard(None)
        controller = CaroController(game, board, menu)
        board._controller = controller
        controller.set_ai_mode()
        controller.start()

    # Gán các hàm này vào menu
    menu.set_callbacks(start_pvp, start_pvai)
    menu.mainloop()
    sys.exit(0)  # Thoát chương trình hoàn toàn sau khi mainloop kết thúc

if __name__ == "__main__":
    main()