from game import TicTacToeGame
from board import TicTacToeBoard
from controller import TicTacToeController

def main():
    game = TicTacToeGame()
    controller = TicTacToeController(game, None)  # Tạm thời truyền None cho board
    board = TicTacToeBoard(controller)  # Truyền controller ngay từ đầu
    controller._board = board  # Gán board vào controller sau khi board được tạo
    board.mainloop()

if __name__ == "__main__":
    main()