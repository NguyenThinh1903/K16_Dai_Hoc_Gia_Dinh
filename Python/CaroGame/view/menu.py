import tkinter as tk
from tkinter import font
import sys

class StartMenu(tk.Tk):
    def __init__(self):
        super().__init__()
        self.title("Caro")
        self.configure(bg="#EDE4D8")  # Warm beige, classic yet soft
        self._center_window()
        self.start_pvp_callback = None
        self.start_pvai_callback = None
        self.host_game_callback = None
        self.join_game_callback = None
        self._create_menu()
        self.protocol("WM_DELETE_WINDOW", self.quit_game)

    def _center_window(self):
        self.update_idletasks()
        screen_width = self.winfo_screenwidth()
        screen_height = self.winfo_screenheight()
        window_width = min(400, screen_width - 100)
        window_height = min(500, screen_height - 100)
        x = (screen_width // 2) - (window_width // 2)
        y = (screen_height // 2) - (window_height // 2)
        self.geometry(f"{window_width}x{window_height}+{x}+{y}")
        self.resizable(True, True)

    def _create_rounded_button(self, x, y, text, command, bg_color="#D9C2A7", active_bg="#C4A78F"):
        # Use Canvas directly for button background
        btn_canvas = tk.Canvas(self, width=300, height=50, highlightthickness=0, bg=self["bg"])
        btn_canvas.place(x=x, y=y)
        btn_canvas.create_rectangle(10, 10, 290, 40, fill=bg_color, outline=bg_color, tags="btn_bg")
        
        # Button text with shadow effect
        btn = tk.Button(btn_canvas, text=text, font=font.Font(family="Arial", size=14, weight="bold"),
                        fg="#3C2F2F", bg=bg_color, bd=0, relief="flat", command=command)
        btn.place(relwidth=1, relheight=1)
        
        # Hover effects
        btn.bind("<Enter>", lambda e: [btn.config(bg=active_bg), btn_canvas.itemconfig("btn_bg", fill=active_bg)])
        btn.bind("<Leave>", lambda e: [btn.config(bg=bg_color), btn_canvas.itemconfig("btn_bg", fill=bg_color)])
        return btn

    def _create_menu(self):
        # Title with classic yet modern touch
        title_label = tk.Label(self, text="Caro", font=font.Font(family="Arial", size=36, weight="bold"),
                               bg="#EDE4D8", fg="#3C2F2F")  # Dark brown for classic warmth
        title_label.place(x=150, y=50)

        # Buttons with subtle, classic-modern design
        self._create_rounded_button(50, 150, "Player vs Player", self.start_pvp)
        self._create_rounded_button(50, 220, "Player vs Machine", self.start_pvai)
        self._create_rounded_button(50, 290, "Host Online Game", self.host_game)
        self._create_rounded_button(50, 360, "Join Online Game", self.join_game)
        self._create_rounded_button(50, 430, "Quit", self.quit_game, "#D99B9B", "#C48787")  # Softer red for Quit

    def set_callbacks(self, start_pvp_callback, start_pvai_callback, host_game_callback, join_game_callback):
        self.start_pvp_callback = start_pvp_callback
        self.start_pvai_callback = start_pvai_callback
        self.host_game_callback = host_game_callback
        self.join_game_callback = join_game_callback

    def start_pvp(self):
        if self.start_pvp_callback:
            self.start_pvp_callback()

    def start_pvai(self):
        if self.start_pvai_callback:
            self.start_pvai_callback()

    def host_game(self):
        if self.host_game_callback:
            self.host_game_callback()

    def join_game(self):
        if self.join_game_callback:
            self.join_game_callback()

    def quit_game(self):
        self.quit()
        self.destroy()
        sys.exit(0)

if __name__ == "__main__":
    app = StartMenu()
    app.mainloop()