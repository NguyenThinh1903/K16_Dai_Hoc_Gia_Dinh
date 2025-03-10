import tkinter as tk
from tkinter import font
import sys  # Thêm import sys để sử dụng sys.exit()

class CaroBoard(tk.Tk):
    def __init__(self, controller):
        super().__init__()
        self.title("Caro")
        self.geometry("600x700")
        self.configure(bg="#f0f0f0")
        self._cells = {}
        self._controller = controller  # Có thể là None ban đầu, sẽ được gán sau
        self._game_over_overlay = None
        self._colors = {
            "X": "#ff0000",
            "O": "#008000",
            "highlight": "#ffff99",
            "suggestion": "#90ee90"
        }
        self._center_window()
        self._create_score_display()
        self._create_board_display()
        self._create_board_grid()
        self._winning_cells = []
        self._played_cells = set()
        self._suggested_cell = None
        # Thoát chương trình khi nhấn "X"
        self.protocol("WM_DELETE_WINDOW", lambda: sys.exit(0))

    def _center_window(self):
        self.update_idletasks()
        screen_width = self.winfo_screenwidth()
        screen_height = self.winfo_screenheight()
        window_width = 600
        window_height = 700
        x = (screen_width // 2) - (window_width // 2)
        y = (screen_height // 2) - (window_height // 2)
        self.geometry(f"{window_width}x{window_height}+{x}+{y}")

    def _create_score_display(self):
        score_frame = tk.Frame(self, bg="#f0f0f0", borderwidth=0)
        score_frame.pack(pady=10)
        self.x_score_label = tk.Label(score_frame, text="X: 0", font=font.Font(family="Arial", size=16), 
                                      bg="#f0f0f0", fg="#000000")
        self.x_score_label.pack(side="left", padx=20)
        self.o_score_label = tk.Label(score_frame, text="O: 0", font=font.Font(family="Arial", size=16), 
                                      bg="#f0f0f0", fg="#000000")
        self.o_score_label.pack(side="right", padx=20)

    def _create_board_display(self):
        self.display = tk.Label(self, text="Ready?", font=font.Font(family="Arial", size=16), bg="#f0f0f0", fg="#000000")
        self.display.pack(pady=10)
        suggest_button = tk.Button(self, text="Suggest Move", font=font.Font(family="Arial", size=12),
                                   bg="#add8e6", fg="#000000", activebackground="#87ceeb", borderwidth=1, relief="flat",
                                   command=self._suggest_move_click)
        suggest_button.pack(pady=5)
        suggest_button.bind("<Enter>", lambda e: suggest_button.config(bg="#87ceeb"))
        suggest_button.bind("<Leave>", lambda e: suggest_button.config(bg="#add8e6"))

    def _create_board_grid(self):
        grid_frame = tk.Frame(self, bg="#f0f0f0")
        grid_frame.pack(pady=20)
        for row in range(15):
            grid_frame.rowconfigure(row, weight=1, minsize=30)
            grid_frame.columnconfigure(row, weight=1, minsize=30)
            for col in range(15):
                button = tk.Button(grid_frame, text="", font=font.Font(family="Arial", size=14, weight="bold"),
                                   bg="#ffffff", fg=self._colors["X"], activebackground="#e0e0e0", 
                                   borderwidth=1, relief="solid", width=2, height=1)
                self._cells[button] = (row, col)
                button.bind("<ButtonPress-1>", self._on_button_click)
                button.bind("<Enter>", lambda e, b=button: b.config(bg="#e0e0e0") if (self._cells[b][0], self._cells[b][1]) not in self._played_cells else None)
                button.bind("<Leave>", lambda e, b=button: b.config(bg="#ffffff") if (self._cells[b][0], self._cells[b][1]) not in self._played_cells else None)
                button.grid(row=row, column=col, padx=0, pady=0, sticky="nsew")

    def _on_button_click(self, event):
        if self._controller and not self._game_over_overlay and not self._controller._is_ai_moving:
            # Chỉ xử lý nếu controller tồn tại và không có overlay hoặc AI đang di chuyển
            clicked_btn = event.widget
            row, col = self._cells[clicked_btn]
            if (row, col) not in self._played_cells:
                self._controller.handle_move(row, col)

    def _suggest_move_click(self):
        if self._controller:
            self._controller.suggest_move()

    def update_button(self, row, col, label):
        print(f"Updating button at [{row}, {col}] with label {label} and color {self._colors[label]}")
        for button, coords in self._cells.items():
            if coords == (row, col):
                button.config(text=label, fg=self._colors[label], bg="#d0d0d0")
                self._played_cells.add((row, col))
                button.update()
                self.update_idletasks()
                break

    def update_display(self, msg, color="black"):
        self.display.config(text=msg, fg=color)
        if "won" in msg.lower() or "tied" in msg.lower():
            self.after(2000, lambda: self._show_game_over_overlay(msg, color))

    def update_score(self, scores):
        self.x_score_label.config(text=f"X: {scores['X']}")
        self.o_score_label.config(text=f"O: {scores['O']}")

    def _show_game_over_overlay(self, msg, color):
        if self._game_over_overlay:
            self._game_over_overlay.destroy()
        self._game_over_overlay = tk.Frame(self, bg="#f0f0f0")
        self._game_over_overlay.place(relwidth=1, relheight=1)
        content_frame = tk.Frame(self._game_over_overlay, bg="#ffffff", borderwidth=1, relief="solid")
        content_frame.place(relx=0.5, rely=0.5, anchor="center", width=250, height=200)
        result_label = tk.Label(content_frame, text=msg, font=font.Font(family="Arial", size=16), bg="#ffffff", fg="#000000")
        result_label.pack(pady=20)
        play_again_btn = tk.Button(content_frame, text="Play Again", font=font.Font(family="Arial", size=12), 
                                   bg="#add8e6", fg="#000000", activebackground="#87ceeb", borderwidth=1, relief="flat",
                                   command=self._controller.reset_game if self._controller else lambda: None)
        play_again_btn.pack(pady=10, padx=20, fill="x")
        play_again_btn.bind("<Enter>", lambda e: play_again_btn.config(bg="#87ceeb"))
        play_again_btn.bind("<Leave>", lambda e: play_again_btn.config(bg="#add8e6"))
        back_to_menu_btn = tk.Button(content_frame, text="Back to Menu", font=font.Font(family="Arial", size=12), 
                                     bg="#ff9999", fg="#000000", activebackground="#ff6666", borderwidth=1, relief="flat",
                                     command=self._controller.back_to_menu if self._controller else lambda: None)
        back_to_menu_btn.pack(pady=10, padx=20, fill="x")
        back_to_menu_btn.bind("<Enter>", lambda e: back_to_menu_btn.config(bg="#ff6666"))
        back_to_menu_btn.bind("<Leave>", lambda e: back_to_menu_btn.config(bg="#ff9999"))

    def highlight_cells(self, winning_combo):
        self._winning_cells = [(row, col) for row, col in winning_combo]
        for row, col in self._winning_cells:
            for button, coords in self._cells.items():
                if coords == (row, col):
                    current_label = button.cget("text")
                    button.config(bg=self._colors["highlight"], fg=self._colors[current_label])
                    button.update()

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
        if self._suggested_cell:
            if (self._suggested_cell.winfo_exists() and 
                (self._cells[self._suggested_cell][0], self._cells[self._suggested_cell][1]) not in self._played_cells):
                self._suggested_cell.config(bg="#ffffff")
            self._suggested_cell = None

    def mainloop(self):
        super().mainloop()

    def highlight_suggested_move(self, row, col):
        if self._suggested_cell:
            if (self._suggested_cell.winfo_exists() and 
                (self._cells[self._suggested_cell][0], self._cells[self._suggested_cell][1]) not in self._played_cells):
                self._suggested_cell.config(bg="#ffffff")
        
        for button, coords in self._cells.items():
            if coords == (row, col) and (row, col) not in self._played_cells:
                button.config(bg=self._colors["suggestion"])
                self._suggested_cell = button
                self.after(2000, lambda b=button: b.config(bg="#ffffff") if b.winfo_exists() and (self._cells[b][0], self._cells[b][1]) not in self._played_cells else None)
                break

    def withdraw(self):
        super().withdraw()