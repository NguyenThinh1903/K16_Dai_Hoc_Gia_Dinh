import tkinter as tk
from tkinter import font

class TicTacToeBoard(tk.Tk):
    def __init__(self, controller):
        super().__init__()
        self.title("Tic-Tac-Toe")
        self.geometry("500x600")
        self.configure(bg="#1e1e1e")
        self._cells = {}
        self._controller = controller
        self._game_over_overlay = None

        self.canvas = tk.Canvas(self, width=500, height=600, highlightthickness=0)
        self.canvas.pack(fill="both", expand=True)
        self._create_gradient()
        self._create_board_display()
        self._create_board_grid()

    def _create_gradient(self):
        """Tạo nền gradient."""
        for i in range(600):
            r = int(30 + (i / 600) * 50)
            color = f"#{r:02x}{r:02x}{r:02x}"
            self.canvas.create_line(0, i, 500, i, fill=color)

    def _create_board_display(self):
        """Tạo khu vực hiển thị thông báo."""
        self.display = tk.Label(
            self.canvas,
            text="Ready?",
            font=font.Font(family="Arial", size=24, weight="bold"),
            bg="#1e1e1e",
            fg="#ffffff"
        )
        self.canvas.create_window(250, 50, window=self.display)
        self.shadow_text = self.canvas.create_text(
            252, 52,
            text="Ready?",
            font=font.Font(family="Arial", size=24, weight="bold"),
            fill="#555555"
        )

    def _create_board_grid(self):
        """Tạo lưới bàn cờ 3x3."""
        grid_frame = tk.Frame(self.canvas, bg="#1e1e1e")
        self.canvas.create_window(250, 350, window=grid_frame)
        for row in range(3):
            grid_frame.rowconfigure(row, weight=1)
            grid_frame.columnconfigure(row, weight=1)
            for col in range(3):
                button = tk.Button(
                    grid_frame,
                    text="",
                    font=font.Font(family="Arial", size=36, weight="bold"),
                    bg="#ffffff",
                    fg="#4682b4",
                    activebackground="#d3d3d3",
                    borderwidth=2,
                    relief="groove",
                    width=5,
                    height=2
                )
                self._cells[button] = (row, col)
                button.bind("<ButtonPress-1>", self._on_button_click)
                button.bind("<Enter>", lambda e, b=button: b.config(bg="#d3d3d3"))
                button.bind("<Leave>", lambda e, b=button: b.config(bg="#ffffff"))
                button.grid(row=row, column=col, padx=5, pady=5, sticky="nsew")

    def _on_button_click(self, event):
        """Gửi sự kiện click đến Controller."""
        if not self._game_over_overlay:
            clicked_btn = event.widget
            row, col = self._cells[clicked_btn]
            self._controller.handle_move(row, col)

    def update_button(self, row, col, label, color):
        """Cập nhật ô trên bàn cờ."""
        for button, coords in self._cells.items():
            if coords == (row, col):
                button.config(text=label, fg=color)
                break

    def update_display(self, msg, color="white"):
        """Cập nhật thông báo và hiển thị overlay khi game kết thúc."""
        self.display.config(text=msg, fg=color)
        self.canvas.itemconfig(self.shadow_text, text=msg)
        if "won" in msg.lower() or "tied" in msg.lower():
            self._show_game_over_overlay(msg, color)

    def _show_game_over_overlay(self, msg, color):
        """Hiển thị overlay khi game kết thúc."""
        if self._game_over_overlay:
            self._game_over_overlay.destroy()

        self._game_over_overlay = tk.Frame(self, bg="#333333")
        self._game_over_overlay.place(relwidth=1, relheight=1)

        content_frame = tk.Frame(self._game_over_overlay, bg="#1e1e1e", borderwidth=2, relief="raised")
        content_frame.place(relx=0.5, rely=0.5, anchor="center", width=300, height=250)

        result_label = tk.Label(
            content_frame,
            text=msg,
            font=font.Font(family="Arial", size=20, weight="bold"),
            bg="#1e1e1e",
            fg=color
        )
        result_label.pack(pady=20)

        play_again_btn = tk.Button(
            content_frame,
            text="Play Again",
            font=font.Font(family="Arial", size=14, weight="bold"),
            bg="#4682b4",
            fg="white",
            command=self._controller.reset_game
        )
        play_again_btn.pack(pady=10)
        play_again_btn.bind("<Enter>", lambda e: play_again_btn.config(bg="#5a9bd4"))
        play_again_btn.bind("<Leave>", lambda e: play_again_btn.config(bg="#4682b4"))

        back_to_menu_btn = tk.Button(
            content_frame,
            text="Back to Menu",
            font=font.Font(family="Arial", size=14, weight="bold"),
            bg="#ff4040",
            fg="white",
            command=self._controller.back_to_menu
        )
        back_to_menu_btn.pack(pady=10)
        back_to_menu_btn.bind("<Enter>", lambda e: back_to_menu_btn.config(bg="#ff6666"))
        back_to_menu_btn.bind("<Leave>", lambda e: back_to_menu_btn.config(bg="#ff4040"))

    def highlight_cells(self, winning_combo):
        """Tô sáng các ô thắng."""
        for button, coordinates in self._cells.items():
            if coordinates in winning_combo:
                button.config(bg="#ff4040", fg="white")

    def reset_board(self):
        """Đặt lại giao diện bàn cờ."""
        self.update_display("Ready?")
        for button in self._cells.keys():
            button.config(bg="#ffffff", text="", fg="#4682b4")
        if self._game_over_overlay:
            self._game_over_overlay.destroy()
            self._game_over_overlay = None