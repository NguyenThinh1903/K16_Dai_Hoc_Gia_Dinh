import tkinter as tk
from tkinter import font

class HostView(tk.Frame):
    def __init__(self, parent, local_ip, on_copy_ip, on_close):
        super().__init__(parent, bg="#EDE4D8", borderwidth=2, relief="raised")
        self.place(relx=0.5, rely=0.5, anchor="center", width=250, height=120)
        self._on_copy_ip = on_copy_ip
        self._on_close = on_close

        ip_frame = tk.Frame(self, bg="#EDE4D8")
        ip_frame.pack(pady=10)
        tk.Label(ip_frame, text=f"IP: {local_ip}", font=font.Font(family="Arial", size=12),
                 bg="#EDE4D8", fg="#3C2F2F").pack(side="left", padx=5)
        tk.Button(ip_frame, text="Copy", font=font.Font(family="Arial", size=10),
                  bg="#D9C2A7", fg="#3C2F2F", command=self._copy_ip).pack(side="left")

        self.status_label = tk.Label(self, text="Share IP with your friend",
                                     font=font.Font(family="Arial", size=10), bg="#EDE4D8", fg="#3C2F2F")
        self.status_label.pack(pady=5)

        tk.Button(self, text="Close", font=font.Font(family="Arial", size=12),
                  bg="#D99B9B", fg="#3C2F2F", command=self._close).pack(pady=5)

    def _copy_ip(self):
        if self._on_copy_ip:
            self._on_copy_ip()

    def _close(self):
        if self._on_close:
            self._on_close()

    def update_status(self, message, color="#3C2F2F"):
        self.status_label.config(text=message, fg=color)