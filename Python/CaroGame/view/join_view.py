import tkinter as tk
from tkinter import font

class JoinView(tk.Frame):
    def __init__(self, parent, on_connect, on_cancel):
        super().__init__(parent, bg="#EDE4D8", borderwidth=2, relief="raised")
        self.place(relx=0.5, rely=0.5, anchor="center", width=250, height=150)
        self._on_connect = on_connect
        self._on_cancel = on_cancel

        tk.Label(self, text="Host IP:", font=font.Font(family="Arial", size=12),
                 bg="#EDE4D8", fg="#3C2F2F").pack(pady=5)
        self.ip_entry = tk.Entry(self, font=font.Font(family="Arial", size=12), width=20)
        self.ip_entry.pack(pady=5)

        self.status_label = tk.Label(self, text="Enter IP to connect",
                                     font=font.Font(family="Arial", size=10), bg="#EDE4D8", fg="#3C2F2F")
        self.status_label.pack(pady=5)

        btn_frame = tk.Frame(self, bg="#EDE4D8")
        btn_frame.pack(pady=5)
        tk.Button(btn_frame, text="Connect", font=font.Font(family="Arial", size=12),
                  bg="#D9C2A7", fg="#3C2F2F", command=self._try_connect).pack(side="left", padx=5)
        tk.Button(btn_frame, text="Cancel", font=font.Font(family="Arial", size=12),
                  bg="#D99B9B", fg="#3C2F2F", command=self._cancel).pack(side="left", padx=5)

    def _try_connect(self):
        ip = self.ip_entry.get().strip()
        if self._on_connect:
            self._on_connect(ip)

    def _cancel(self):
        if self._on_cancel:
            self._on_cancel()

    def update_status(self, message, color="#3C2F2F"):
        self.status_label.config(text=message, fg=color)