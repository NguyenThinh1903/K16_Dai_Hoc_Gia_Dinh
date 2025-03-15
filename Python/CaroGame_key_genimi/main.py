import sys
from view.menu import StartMenu
from model.game import CaroGame
from controller.controller import CaroController
from view.board import CaroBoard

def main():
    # Khởi tạo menu
    menu = StartMenu()
    
    # Định nghĩa các hàm khởi tạo game
    def start_pvp():
        menu.withdraw()  # Ẩn menu
        game = CaroGame()
        board = CaroBoard(None)  # Tạm thời tạo board, sau đó gán controller
        controller = CaroController(game, board, menu)
        board._controller = controller  # Gán controller cho board
        controller.start()  # Gọi start() nhưng không gọi board.mainloop() ở đây

    def start_pvai():
        menu.withdraw()  # Ẩn menu
        game = CaroGame()
        board = CaroBoard(None)  # Tạm thời tạo board, sau đó gán controller
        controller = CaroController(game, board, menu)
        board._controller = controller  # Gán controller cho board
        controller.set_ai_mode()
        controller.start()  # Gọi start() nhưng không gọi board.mainloop() ở đây

    # Gán các hàm này vào menu
    menu.set_callbacks(start_pvp, start_pvai)
    menu.mainloop()  # Chỉ gọi mainloop một lần ở đây
    sys.exit(0)  # Thoát hoàn toàn sau khi mainloop kết thúc

if __name__ == "__main__":
    main()