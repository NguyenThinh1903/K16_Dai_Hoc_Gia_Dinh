"""
Bài 3: Quản lý Sách trong Thư viện
Yêu cầu:
1.	Xây dựng lớp Sach với các thuộc tính:
o	ten_sach, tac_gia, nam_xuat_ban, gia_ban.
2.	Các phương thức:
o	thong_tin(): Trả về thông tin sách dưới dạng chuỗi.
Chương trình chính:
-	Nhập thông tin sách từ bàn phím và hiển thị thông tin sách.
"""


class Sach:
    def __init__(self, ten_sach: str, tac_gia: str, nam_xuat_ban: int, gia_ban: float):
        self.ten_sach = ten_sach
        self.tac_gia = tac_gia
        self.nam_xuat_ban = nam_xuat_ban
        self.gia_ban = gia_ban

    def thong_tin(self) -> str:
        return (f"Tên sách: {self.ten_sach}, "
                f"Tác giả: {self.tac_gia}, "
                f"Năm xuất bản: {self.nam_xuat_ban}, "
                f"Giá bán: {self.gia_ban} VND")

if __name__ == "__main__":
    # Nhập thông tin sách từ bàn phím với kiểm tra lỗi
    while True:
        try:
            ten_sach = input("Nhập tên sách: ").strip()
            if not ten_sach:
                print("Tên sách không được để trống. Vui lòng nhập lại!")
                continue
            
            tac_gia = input("Nhập tên tác giả: ").strip()
            if not tac_gia:
                print("Tên tác giả không được để trống. Vui lòng nhập lại!")
                continue
            
            nam_xuat_ban = int(input("Nhập năm xuất bản: "))
            if nam_xuat_ban < 1000 or nam_xuat_ban > 2025:  # năm hiện tại là 2025
                print("Năm xuất bản phải từ 1000 đến 2025. Vui lòng nhập lại!")
                continue
            
            gia_ban = float(input("Nhập giá bán (VND): "))
            if gia_ban < 0:
                print("Giá bán phải là số không âm. Vui lòng nhập lại!")
                continue
            
            break  # Thoát vòng lặp nếu tất cả thông tin hợp lệ
        except ValueError:
            print("Vui lòng nhập số hợp lệ cho năm xuất bản hoặc giá bán!")

    # Tạo đối tượng sách và hiển thị thông tin
    sach = Sach(ten_sach, tac_gia, nam_xuat_ban, gia_ban)
    print("\nThông tin sách vừa nhập:")
    print(sach.thong_tin())