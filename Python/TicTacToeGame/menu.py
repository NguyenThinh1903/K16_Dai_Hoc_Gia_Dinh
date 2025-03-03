import tkinter as tk
from tkinter import font
from game import TicTacToeGame
from controller import TicTacToeController

class StartMenu(tk.Tk):
    def __init__(self):
        super().__init__()
        self.title("Tic-Tac-Toe")
        self.geometry("400x500")
        self.configure(bg="#1e1e1e")
        self.canvas = tk.Canvas(self, width=400, height=500, highlightthickness=0)
        self.canvas.pack(fill="both", expand=True)
        self._create_gradient()
        self._create_menu()

    def _create_gradient(self):
        for i in range(500):
            r = int(30 + (i / 500) * 50)
            g = r
            b = r
            color = f"#{r:02x}{g:02x}{b:02x}"
            self.canvas.create_line(0, i, 400, i, fill=color)

    def _create_menu(self):
        self.canvas.create_text(
            200, 100,
            text="Tic-Tac-Toe",
            font=font.Font(family="Arial", size=36, weight="bold"),
            fill="#ffffff",
            tags="title"
        )
        self.canvas.create_text(
            202, 102,
            text="Tic-Tac-Toe",
            font=font.Font(family="Arial", size=36, weight="bold"),
            fill="#555555",
            tags="title_shadow"
        )

        pvp_button = tk.Button(
            self,
            text="Player vs Player",
            font=font.Font(family="Arial", size=14, weight="bold"),
            bg="#4682b4",
            fg="white",
            activebackground="#5a9bd4",
            borderwidth=2,
            relief="flat",
            command=self.start_pvp
        )
        pvp_button.place(x=100, y=200, width=200, height=50)
        pvp_button.bind("<Enter>", lambda e: pvp_button.config(bg="#5a9bd4"))
        pvp_button.bind("<Leave>", lambda e: pvp_button.config(bg="#4682b4"))

        pvai_button = tk.Button(
            self,
            text="Player vs AI",
            font=font.Font(family="Arial", size=14, weight="bold"),
            bg="#4682b4",
            fg="white",
            activebackground="#5a9bd4",
            borderwidth=2,
            relief="flat",
            command=self.start_pvai
        )
        pvai_button.place(x=100, y=270, width=200, height=50)
        pvai_button.bind("<Enter>", lambda e: pvai_button.config(bg="#5a9bd4"))
        pvai_button.bind("<Leave>", lambda e: pvai_button.config(bg="#4682b4"))

        quit_button = tk.Button(
            self,
            text="Quit",
            font=font.Font(family="Arial", size=14, weight="bold"),
            bg="#ff4040",
            fg="white",
            activebackground="#ff6666",
            borderwidth=2,
            relief="flat",
            command=self.quit_game
        )
        quit_button.place(x=100, y=340, width=200, height=50)
        quit_button.bind("<Enter>", lambda e: quit_button.config(bg="#ff6666"))
        quit_button.bind("<Leave>", lambda e: quit_button.config(bg="#ff4040"))

    def start_pvp(self):
        self.destroy()
        game = TicTacToeGame()
        controller = TicTacToeController(game)
        controller.start()

    def start_pvai(self):
        self.destroy()
        game = TicTacToeGame()
        controller = TicTacToeController(game)
        controller.set_ai_mode()
        controller.start()

    def quit_game(self):
        self.quit()
        self.destroy()

if __name__ == "__main__":
    menu = StartMenu()
    menu.mainloop()