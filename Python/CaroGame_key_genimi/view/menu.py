import sys
import tkinter as tk
from tkinter import font

class StartMenu(tk.Tk):
    def __init__(self):
        super().__init__()
        self.title("Caro")
        self.geometry("400x500")
        self.configure(bg="#f0f0f0")
        self._center_window()
        self.start_pvp_callback = None
        self.start_pvai_callback = None
        self._create_menu()
        self.protocol("WM_DELETE_WINDOW", self.quit_game)  # Thoát khi nhấn "X"

    def _center_window(self):
        self.update_idletasks()
        screen_width = self.winfo_screenwidth()
        screen_height = self.winfo_screenheight()
        window_width = 400
        window_height = 500
        x = (screen_width // 2) - (window_width // 2)
        y = (screen_height // 2) - (window_height // 2)
        self.geometry(f"{window_width}x{window_height}+{x}+{y}")

    def _create_menu(self):
        title_label = tk.Label(self, text="Caro", font=font.Font(family="Arial", size=36, weight="bold"), 
                               bg="#f0f0f0", fg="#000000")
        title_label.pack(pady=50)

        pvp_button = tk.Button(self, text="Player vs Player", font=font.Font(family="Arial", size=14), 
                               bg="#add8e6", fg="#000000", activebackground="#87ceeb", borderwidth=1, relief="flat", 
                               command=self.start_pvp)
        pvp_button.pack(pady=20, padx=50, fill="x")
        pvp_button.bind("<Enter>", lambda e: pvp_button.config(bg="#87ceeb"))
        pvp_button.bind("<Leave>", lambda e: pvp_button.config(bg="#add8e6"))

        pvai_button = tk.Button(self, text="Player vs Machine", font=font.Font(family="Arial", size=14), 
                                bg="#add8e6", fg="#000000", activebackground="#87ceeb", borderwidth=1, relief="flat", 
                                command=self.start_pvai)
        pvai_button.pack(pady=20, padx=50, fill="x")
        pvai_button.bind("<Enter>", lambda e: pvai_button.config(bg="#87ceeb"))
        pvai_button.bind("<Leave>", lambda e: pvai_button.config(bg="#add8e6"))

        quit_button = tk.Button(self, text="Quit", font=font.Font(family="Arial", size=14), 
                                bg="#ff9999", fg="#000000", activebackground="#ff6666", borderwidth=1, relief="flat", 
                                command=self.quit_game)
        quit_button.pack(pady=20, padx=50, fill="x")
        quit_button.bind("<Enter>", lambda e: quit_button.config(bg="#ff6666"))
        quit_button.bind("<Leave>", lambda e: quit_button.config(bg="#ff9999"))

    def set_callbacks(self, start_pvp_callback, start_pvai_callback):
        self.start_pvp_callback = start_pvp_callback
        self.start_pvai_callback = start_pvai_callback

    def start_pvp(self):
        if self.start_pvp_callback:
            self.start_pvp_callback()
        else:
            print("start_pvp_callback not set!")

    def start_pvai(self):
        if self.start_pvai_callback:
            self.start_pvai_callback()
        else:
            print("start_pvai_callback not set!")

    def quit_game(self):
        self.quit()  # Thoát Tkinter mainloop
        self.destroy()  # Đóng cửa sổ Tkinter
        sys.exit(0)  # Thoát hoàn toàn chương trình Python