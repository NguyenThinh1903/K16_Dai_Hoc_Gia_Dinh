import tkinter as tk
from tkinter import font
import sys

class CaroBoard(tk.Tk):
    def __init__(self, controller):
        print("Initializing CaroBoard...")  # Debug
        super().__init__()
        self.title("Caro")
        self.configure(bg="#f0f0f0")
        self._cells = {}
        self._controller = controller
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
        self._create_chat_display()
        self._winning_cells = []
        self._played_cells = set()
        self._suggested_cell = None
        self._is_resetting = False
        self.protocol("WM_DELETE_WINDOW", lambda: sys.exit(0))
        print("CaroBoard initialized successfully")  # Debug

    def _center_window(self):
        print("Centering window...")  # Debug
        self.update_idletasks()
        screen_width = self.winfo_screenwidth()
        screen_height = self.winfo_screenheight()
        window_width = min(600, screen_width - 100)
        window_height = min(700, screen_height - 100)
        x = (screen_width // 2) - (window_width // 2)
        y = (screen_height // 2) - (window_height // 2)
        self.geometry(f"{window_width}x{window_height}+{x}+{y}")
        self.resizable(True, True)

    def _create_score_display(self):
        print("Creating score display...")  # Debug
        score_frame = tk.Frame(self, bg="#f0f0f0", borderwidth=0)
        score_frame.pack(pady=10)
        self.x_score_label = tk.Label(score_frame, text="X: 0", font=font.Font(family="Arial", size=16), bg="#f0f0f0", fg="#000000")
        self.x_score_label.pack(side="left", padx=20)
        self.o_score_label = tk.Label(score_frame, text="O: 0", font=font.Font(family="Arial", size=16), bg="#f0f0f0", fg="#000000")
        self.o_score_label.pack(side="right", padx=20)

    def _create_board_display(self):
        print("Creating board display...")  # Debug
        self.display = tk.Label(self, text="Ready?", font=font.Font(family="Arial", size=16), bg="#f0f0f0", fg="#000000")
        self.display.pack(pady=10)
        suggest_button = tk.Button(self, text="Suggest Move", font=font.Font(family="Arial", size=12),
                                   bg="#add8e6", fg="#000000", activebackground="#87ceeb", borderwidth=1, relief="flat",
                                   command=self._suggest_move_click, padx=10, pady=5)
        suggest_button.pack(pady=5)
        suggest_button.bind("<Enter>", lambda e: suggest_button.config(bg="#87ceeb"))
        suggest_button.bind("<Leave>", lambda e: suggest_button.config(bg="#add8e6"))

    def _create_board_grid(self):
        print("Creating board grid...")  # Debug
        self.grid_frame = tk.Frame(self, bg="#f0f0f0")
        self.grid_frame.pack(pady=20, expand=True, fill="both")
        for row in range(15):
            self.grid_frame.rowconfigure(row, weight=1, minsize=30)
            self.grid_frame.columnconfigure(row, weight=1, minsize=30)
            for col in range(15):
                button = tk.Button(self.grid_frame, text="", font=font.Font(family="Arial", size=14, weight="bold"),
                                   bg="#ffffff", fg=self._colors["X"], activebackground="#e0e0e0", borderwidth=1,
                                   relief="solid", width=2, height=1)
                button.grid(row=row, column=col, padx=0, pady=0, sticky="nsew")
                self._cells[button] = (row, col)
                button.bind("<ButtonPress-1>", self._on_button_click)
                button.bind("<Enter>", lambda e, b=button: b.config(bg="#e0e0e0") if (self._cells[b][0], self._cells[b][1]) not in self._played_cells else None)
                button.bind("<Leave>", lambda e, b=button: b.config(bg="#ffffff") if (self._cells[b][0], self._cells[b][1]) not in self._played_cells else None)

    def _create_chat_display(self):
        print("Creating chat display...")  # Debug
        chat_frame = tk.Frame(self, bg="#f0f0f0")
        chat_frame.pack(pady=5)

        self.chat_display = tk.Text(chat_frame, height=5, width=30, state='disabled', wrap=tk.WORD)
        self.chat_display.pack(side="top", padx=5, pady=5)

        self.chat_entry = tk.Entry(chat_frame, width=30)
        self.chat_entry.pack(side="left", padx=5, pady=5)

        send_button = tk.Button(chat_frame, text="Send", command=self._send_chat)
        send_button.pack(side="left", padx=5, pady=5)

    def _send_chat(self):
        if self._controller:
            message = self.chat_entry.get()
            if message:
                self._controller.send_chat_message(message)
                self.chat_entry.delete(0, tk.END)

    def _on_button_click(self, event):
        if self._controller and not self._game_over_overlay:
            if hasattr(self._controller, '_is_ai_moving'):
                if self._controller._is_ai_moving:
                    return
            clicked_btn = event.widget
            row, col = self._cells[clicked_btn]
            if (row, col) not in self._played_cells:
                self._controller.handle_move(row, col)

    def _suggest_move_click(self):
        if self._controller:
            self._controller.suggest_move()

    def update_button(self, row, col, label):
        for button, coords in self._cells.items():
            if coords == (row, col):
                button.config(text=label, fg=self._colors[label], bg="#d0d0d0")
                self._played_cells.add((row, col))
                break

    def update_display(self, msg, color="black"):
        self.display.config(text=msg, fg=color)
        if "won" in msg.lower() or "tied" in msg.lower():
            self.after(2000, lambda: self._show_game_over_overlay(msg))

    def update_score(self, scores):
        self.x_score_label.config(text=f"X: {scores['X']}")
        self.o_score_label.config(text=f"O: {scores['O']}")

    def _show_game_over_overlay(self, msg):
        if self._game_over_overlay:
            self._game_over_overlay.destroy()

        for button in self._cells.keys():
            button.config(state="disabled")
        if hasattr(self._controller, '_rematch_button') and self._controller._rematch_button:
            self._controller._rematch_button.config(state="disabled")

        self._game_over_overlay = tk.Frame(self, bg="#ffffff", borderwidth=2, relief="solid")
        self._game_over_overlay.place(relx=0.5, rely=0.5, anchor="center", width=250, height=200)

        result_label = tk.Label(self._game_over_overlay, text=msg, font=font.Font(family="Arial", size=16), bg="#ffffff", fg="#000000")
        result_label.pack(pady=20)

        play_again_btn = tk.Button(self._game_over_overlay, text="Play Again", font=font.Font(family="Arial", size=12),
                                   bg="#add8e6", fg="#000000", activebackground="#87ceeb", borderwidth=1, relief="flat",
                                   command=self._safe_reset_game, padx=10, pady=5)
        play_again_btn.pack(pady=10, padx=20, fill="x")
        play_again_btn.bind("<Enter>", lambda e: play_again_btn.config(bg="#87ceeb"))
        play_again_btn.bind("<Leave>", lambda e: play_again_btn.config(bg="#add8e6"))

        back_to_menu_btn = tk.Button(self._game_over_overlay, text="Back to Menu", font=font.Font(family="Arial", size=12),
                                     bg="#ff9999", fg="#000000", activebackground="#ff6666", borderwidth=1, relief="flat",
                                     command=self._controller.back_to_menu if self._controller else lambda: None, padx=10, pady=5)
        back_to_menu_btn.pack(pady=5, padx=20, fill="x")
        back_to_menu_btn.bind("<Enter>", lambda e: back_to_menu_btn.config(bg="#ff6666"))
        back_to_menu_btn.bind("<Leave>", lambda e: back_to_menu_btn.config(bg="#ff9999"))

        self._fade_in(self._game_over_overlay)

    def _safe_reset_game(self):
        if not self._is_resetting:
            self._is_resetting = True
            try:
                if self._controller:
                    self._controller.reset_game()
            finally:
                self._is_resetting = False

    def _fade_in(self, widget, alpha=0.0):
        alpha += 0.05
        if alpha <= 1.0:
            widget.configure(bg=f"#{int(255 * alpha):02x}{int(255 * alpha):02x}{int(255 * alpha):02x}")
            self.after(50, lambda: self._fade_in(widget, alpha))

    def highlight_cells(self, winning_combo):
        self._winning_cells = [(row, col) for row, col in winning_combo]
        for row, col in self._winning_cells:
            for button, coords in self._cells.items():
                if coords == (row, col):
                    current_label = button.cget("text")
                    button.config(bg=self._colors["highlight"], fg=self._colors[current_label])
                    break

    def reset_board(self):
        print("Resetting board...")  # Debug
        self.update_display("Ready?")
        for button in self._cells.keys():
            button.config(bg="#ffffff", text="", state="normal")
        self._played_cells.clear()
        if self._game_over_overlay:
            try:
                self._game_over_overlay.destroy()
                print("Overlay destroyed")  # Debug
            except tk.TclError:
                print("Overlay already destroyed or invalid")  # Debug
            self._game_over_overlay = None

        if self._suggested_cell:
            if (self._suggested_cell.winfo_exists() and
                (self._cells[self._suggested_cell][0], self._cells[self._suggested_cell][1]) not in self._played_cells):
                self._suggested_cell.config(bg="#ffffff")
            self._suggested_cell = None

        if self._winning_cells:
            for row, col in self._winning_cells:
                for button, coords in self._cells.items():
                    if coords == (row, col):
                        button.config(bg="#ffffff")
            self._winning_cells = []

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

    def mainloop(self):
        print("Starting Tkinter mainloop...")  # Debug
        super().mainloop()