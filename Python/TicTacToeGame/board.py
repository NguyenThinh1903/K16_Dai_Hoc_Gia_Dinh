import tkinter as tk
from tkinter import font

class TicTacToeBoard(tk.Tk):
    def __init__(self, controller):
        super().__init__()
        self.title("Tic-Tac-Toe Game")
        self._cells = {}
        self._controller = controller
        self._create_menu()
        self._create_board_display()
        self._create_board_grid()

    def _create_menu(self):
        menu_bar = tk.Menu(master=self)
        self.config(menu=menu_bar)
        file_menu = tk.Menu(master=menu_bar)
        file_menu.add_command(label="Play Again", command=self._controller.reset_game)
        file_menu.add_command(label="Play with AI", command=self._controller.set_ai_mode)
        file_menu.add_separator()
        file_menu.add_command(label="Exit", command=quit)
        menu_bar.add_cascade(label="File", menu=file_menu)

    def _create_board_display(self):
        display_frame = tk.Frame(master=self)
        display_frame.pack(fill=tk.X)
        self.display = tk.Label(
            master=display_frame,
            text="Ready?",
            font=font.Font(size=28, weight="bold"),
        )
        self.display.pack()

    def _create_board_grid(self):
        grid_frame = tk.Frame(master=self)
        grid_frame.pack()
        for row in range(3):
            self.rowconfigure(row, weight=1, minsize=50)
            self.columnconfigure(row, weight=1, minsize=75)
            for col in range(3):
                button = tk.Button(
                    master=grid_frame,
                    text="",
                    font=font.Font(size=36, weight="bold"),
                    fg="black",
                    width=3,
                    height=2,
                    highlightbackground="lightblue",
                )
                self._cells[button] = (row, col)
                button.bind("<ButtonPress-1>", self._on_button_click)
                button.grid(row=row, column=col, padx=5, pady=5, sticky="nsew")

    def _on_button_click(self, event):
        clicked_btn = event.widget
        row, col = self._cells[clicked_btn]
        self._controller.handle_move(row, col)

    def update_button(self, row, col, label, color):
        for button, coords in self._cells.items():
            if coords == (row, col):
                button.config(text=label, fg=color)
                break

    def update_display(self, msg, color="black"):
        self.display["text"] = msg
        self.display["fg"] = color

    def highlight_cells(self, winning_combo):
        for button, coordinates in self._cells.items():
            if coordinates in winning_combo:
                button.config(highlightbackground="red")

    def reset_board(self):
        self.update_display("Ready?")
        for button in self._cells.keys():
            button.config(highlightbackground="lightblue", text="", fg="black")