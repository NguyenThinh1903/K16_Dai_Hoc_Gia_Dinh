import tkinter as tk
from tkinter import font

class CaroBoard(tk.Tk):
    def __init__(self, controller):
        super().__init__()
        self.title("Caro")
        self.geometry("800x900")
        self.configure(bg="#1e1e1e")
        self._cells = {}
        self._controller = controller
        self._game_over_overlay = None
        # Định nghĩa màu sắc
        self._colors = {
            "X": "#ff0000",        # Đỏ đậm cho X
            "O": "#008000",        # Xanh lá đậm cho O
            "highlight": "#ffff00"  # Vàng sáng cho nhấp nháy chiến thắng
        }
        self._center_window()
        self.canvas = tk.Canvas(self, width=800, height=900, highlightthickness=0)
        self.canvas.pack(fill="both", expand=True)
        self._create_gradient()
        self._create_score_display()
        self._create_board_display()
        self._create_board_grid()
        self._winning_cells = []
        self._blink_on = False
        self._blink_count = 0
        self._blink_interval = 300
        self._played_cells = set()

    def _center_window(self):
        self.update_idletasks()
        screen_width = self.winfo_screenwidth()
        screen_height = self.winfo_screenheight()
        window_width = 800
        window_height = 900
        x = (screen_width // 2) - (window_width // 2)
        y = (screen_height // 2) - (window_height // 2)
        self.geometry(f"{window_width}x{window_height}+{x}+{y}")

    def _create_gradient(self):
        for i in range(900):
            r = int(30 + (i / 900) * 50)
            color = f"#{r:02x}{r:02x}{r:02x}"
            self.canvas.create_line(0, i, 800, i, fill=color)

    def _create_score_display(self):
        score_frame = tk.Frame(self.canvas, bg="#2e2e2e", borderwidth=2, relief="groove")
        self.canvas.create_window(400, 50, window=score_frame)
        self.x_score_label = tk.Label(score_frame, text="X: 0", font=font.Font(family="Arial", size=20, weight="bold"), 
                                      bg="#2e2e2e", fg=self._colors["X"], width=6)
        self.x_score_label.pack(side="left", padx=20, pady=5)
        self.o_score_label = tk.Label(score_frame, text="O: 0", font=font.Font(family="Arial", size=20, weight="bold"), 
                                      bg="#2e2e2e", fg=self._colors["O"], width=6)
        self.o_score_label.pack(side="right", padx=20, pady=5)

    def _create_board_display(self):
        self.display = tk.Label(self.canvas, text="Ready?", font=font.Font(family="Arial", size=24, weight="bold"), bg="#1e1e1e", fg="#ffffff")
        self.canvas.create_window(400, 120, window=self.display)
        self.shadow_text = self.canvas.create_text(402, 122, text="Ready?", font=font.Font(family="Arial", size=24, weight="bold"), fill="#555555")

    def _create_board_grid(self):
        grid_frame = tk.Frame(self.canvas, bg="#1e1e1e")
        self.canvas.create_window(400, 500, window=grid_frame)
        for row in range(15):
            grid_frame.rowconfigure(row, weight=1, minsize=30)
            grid_frame.columnconfigure(row, weight=1, minsize=30)
            for col in range(15):
                button = tk.Button(grid_frame, text="", font=font.Font(family="Arial", size=14, weight="bold"),
                                   bg="#ffffff", fg=self._colors["X"], activebackground="#d3d3d3", 
                                   borderwidth=1, relief="solid", width=2, height=1)
                self._cells[button] = (row, col)
                button.bind("<ButtonPress-1>", self._on_button_click)
                button.bind("<Enter>", lambda e, b=button: b.config(bg="#e0e0e0") if (self._cells[b][0], self._cells[b][1]) not in self._played_cells else None)
                button.bind("<Leave>", lambda e, b=button: b.config(bg="#ffffff") if (self._cells[b][0], self._cells[b][1]) not in self._played_cells else None)
                button.grid(row=row, column=col, padx=0, pady=0, sticky="nsew")

    def _on_button_click(self, event):
        if not self._game_over_overlay:
            clicked_btn = event.widget
            row, col = self._cells[clicked_btn]
            if (row, col) not in self._played_cells:
                self._controller.handle_move(row, col)

    def update_button(self, row, col, label):
        print(f"Updating button at [{row}, {col}] with label {label} and color {self._colors[label]}")
        for button, coords in self._cells.items():
            if coords == (row, col):
                current_fg = button.cget("fg")
                print(f"Current fg color before update: {current_fg}")
                button.config(text=label, fg=self._colors[label], bg="#d0d0d0")
                self._played_cells.add((row, col))
                button.update()
                self.update_idletasks()
                print(f"Current fg color after update: {button.cget('fg')}")
                break

    def update_display(self, msg, color="white"):
        self.display.config(text=msg, fg=color)
        self.canvas.itemconfig(self.shadow_text, text=msg)
        if "won" in msg.lower() or "tied" in msg.lower():
            self.after(3000, lambda: self._show_game_over_overlay(msg, color))

    def update_score(self, scores):
        self.x_score_label.config(text=f"X: {scores['X']}")
        self.o_score_label.config(text=f"O: {scores['O']}")

    def _show_game_over_overlay(self, msg, color):
        if self._game_over_overlay:
            self._game_over_overlay.destroy()
        self._game_over_overlay = tk.Frame(self, bg="#333333")
        self._game_over_overlay.place(relwidth=1, relheight=1)
        content_frame = tk.Frame(self._game_over_overlay, bg="#1e1e1e", borderwidth=2, relief="raised")
        content_frame.place(relx=0.5, rely=0.5, anchor="center", width=300, height=250)
        result_label = tk.Label(content_frame, text=msg, font=font.Font(family="Arial", size=20, weight="bold"), bg="#1e1e1e", fg=color)
        result_label.pack(pady=20)
        play_again_btn = tk.Button(content_frame, text="Play Again", font=font.Font(family="Arial", size=14, weight="bold"), 
                                   bg="#4682b4", fg="white", command=self._controller.reset_game)
        play_again_btn.pack(pady=10)
        play_again_btn.bind("<Enter>", lambda e: play_again_btn.config(bg="#5a9bd4"))
        play_again_btn.bind("<Leave>", lambda e: play_again_btn.config(bg="#4682b4"))
        back_to_menu_btn = tk.Button(content_frame, text="Back to Menu", font=font.Font(family="Arial", size=14, weight="bold"), 
                                     bg="#ff4040", fg="white", command=self._controller.back_to_menu)
        back_to_menu_btn.pack(pady=10)
        back_to_menu_btn.bind("<Enter>", lambda e: back_to_menu_btn.config(bg="#ff6666"))
        back_to_menu_btn.bind("<Leave>", lambda e: back_to_menu_btn.config(bg="#ff4040"))

    def highlight_cells(self, winning_combo):
        self._winning_cells = [(row, col) for row, col in winning_combo]
        self._blink_count = 0
        self._blink_on = True
        self._blink()

    def _blink(self):
        if self._blink_count < 10:
            self._blink_on = not self._blink_on
            for row, col in self._winning_cells:
                for button, coords in self._cells.items():
                    if coords == (row, col):
                        # Dùng màu highlight riêng thay vì màu của X/O
                        button.config(bg=self._colors["highlight"] if self._blink_on else "#d0d0d0")
                        button.update()
            self._blink_count += 1
            self.after(self._blink_interval, self._blink)
        else:
            for row, col in self._winning_cells:
                for button, coords in self._cells.items():
                    if coords == (row, col):
                        button.config(bg="#d0d0d0")
            self._winning_cells = []
            self._blink_count = 0
            self._blink_on = False

    def reset_board(self):
        self.update_display("Ready?")
        for button in self._cells.keys():
            button.config(bg="#ffffff", text="", fg=self._colors["X"], state="normal")
        self._played_cells.clear()
        if self._game_over_overlay:
            self._game_over_overlay.destroy()
            self._game_over_overlay = None
        if self._winning_cells:
            for row, col in self._winning_cells:
                for button, coords in self._cells.items():
                    if coords == (row, col):
                        button.config(bg="#ffffff")
            self._winning_cells = []

    def mainloop(self):
        super().mainloop()