import math

class Hinh:
    def __init__(self, mau_sac="đỏ"):
        self.mau_sac = mau_sac

    def __repr__(self):
        return f"Hinh(mau_sac={self.mau_sac!r})"

    def __str__(self):
        return f"Hình có màu {self.mau_sac}"

class HinhTron(Hinh):
    def __init__(self, ban_kinh, mau_sac="đỏ"):
        if ban_kinh <= 0:
            raise ValueError("Bán kính phải lớn hơn 0.")
        super().__init__(mau_sac)
        self.ban_kinh = ban_kinh

    def __repr__(self):
        return f"HinhTron(ban_kinh={self.ban_kinh}, mau_sac={self.mau_sac!r})"

    def __str__(self):
        return f"Hình tròn có bán kính {self.ban_kinh} và màu {self.mau_sac}"

    def tinh_dien_tich(self):
        return math.pi * self.ban_kinh ** 2

    def tinh_chu_vi(self):
        return 2 * math.pi * self.ban_kinh

class HinhChuNhat(Hinh):
    def __init__(self, chieu_dai, chieu_rong, mau_sac="đỏ"):
        if chieu_dai <= 0 or chieu_rong <= 0:
            raise ValueError("Chiều dài và chiều rộng phải lớn hơn 0.")
        super().__init__(mau_sac)
        self.chieu_dai = chieu_dai
        self.chieu_rong = chieu_rong

    def __repr__(self):
        return f"HinhChuNhat(chieu_dai={self.chieu_dai}, chieu_rong={self.chieu_rong}, mau_sac={self.mau_sac!r})"

    def __str__(self):
        return f"Hình chữ nhật có chiều dài {self.chieu_dai}, chiều rộng {self.chieu_rong} và màu {self.mau_sac}"

    def tinh_dien_tich(self):
        return self.chieu_dai * self.chieu_rong

    def tinh_chu_vi(self):
        return 2 * (self.chieu_dai + self.chieu_rong)

class HinhVuong(HinhChuNhat):
    def __init__(self, canh, mau_sac="đỏ"):
        if canh <= 0:
            raise ValueError("Cạnh hình vuông phải lớn hơn 0.")
        super().__init__(canh, canh, mau_sac)

    def __repr__(self):
        return f"HinhVuong(canh={self.chieu_dai}, mau_sac={self.mau_sac!r})"

    def __str__(self):
        return f"Hình vuông có cạnh {self.chieu_dai} và màu {self.mau_sac}"

# Tạo các đối tượng
hinh_tron = HinhTron(1.0, "đỏ")
hinh_chu_nhat = HinhChuNhat(2.0, 3.0, "xanh")
hinh_vuong = HinhVuong(3.0, "vàng")

# In thông tin các đối tượng
print(repr(hinh_tron))
print("Diện tích hình tròn:", hinh_tron.tinh_dien_tich())
print("Chu vi hình tròn:", hinh_tron.tinh_chu_vi())
print()

print(repr(hinh_chu_nhat))
print("Diện tích hình chữ nhật:", hinh_chu_nhat.tinh_dien_tich())
print("Chu vi hình chữ nhật:", hinh_chu_nhat.tinh_chu_vi())
print()

print(repr(hinh_vuong))
print("Diện tích hình vuông:", hinh_vuong.tinh_dien_tich())
print("Chu vi hình vuông:", hinh_vuong.tinh_chu_vi())
print()
