"""
Bài 2: Quản lý Sinh viên
Viết một chương trình Python sử dụng lập trình hướng đối tượng để quản lý thông tin sinh viên.
Yêu cầu:
1.	Xây dựng lớp SinhVien với các thuộc tính và phương thức sau:
-	Thuộc tính: ho_ten, ma_sv, diem_toan, diem_ly, diem_hoa
-	Constructor (__init__) để khởi tạo thông tin sinh viên
-	Phương thức:
	diem_trung_binh(): Tính điểm trung bình 3 môn
	xep_loai(): Xếp loại học lực theo điểm trung bình
	thong_tin(): Hiển thị thông tin sinh viên
2.	Chương trình chính:
-	Yêu cầu nhập thông tin sinh viên từ bàn phím
-	Tạo đối tượng SinhVien
-	Hiển thị điểm trung bình, xếp loại và thông tin
Quy tắc xếp loại:
-	>= 8.5: Giỏi
-	7.0 - 8.4: Khá
-	5.0 - 6.9: Trung bình
-	< 5.0: Yếu
"""

class SinhVien:
    def __init__(self, ho_ten: str, ma_sv: str, diem_toan: float, diem_ly: float, diem_hoa: float):
        self.ho_ten = ho_ten
        self.ma_sv = ma_sv
        self.diem_toan = diem_toan
        self.diem_ly = diem_ly
        self.diem_hoa = diem_hoa

    def diem_trung_binh(self) -> float:
        return round((self.diem_toan + self.diem_ly + self.diem_hoa) / 3, 2)

    def xep_loai(self) -> str:
        dtb = self.diem_trung_binh()
        if dtb >= 8.5:
            return "Giỏi"
        elif dtb >= 7.0:
            return "Khá"
        elif dtb >= 5.0:
            return "Trung bình"
        else:
            return "Yếu"

    def thong_tin(self) -> str:
        return (f"Sinh viên: {self.ho_ten} (Mã SV: {self.ma_sv})\n"
                f"Điểm trung bình: {self.diem_trung_binh()} - Xếp loại: {self.xep_loai()}")

# Chạy thử chương trình
if __name__ == "__main__":
    while True:
        try:
            ho_ten = input("Nhập họ tên sinh viên: ")
            ma_sv = input("Nhập mã sinh viên: ")
            diem_toan = float(input("Nhập điểm Toán: "))
            diem_ly = float(input("Nhập điểm Lý: "))
            diem_hoa = float(input("Nhập điểm Hóa: "))
            if 0 <= diem_toan <= 10 and 0 <= diem_ly <= 10 and 0 <= diem_hoa <= 10:
                break
            else:
                print("Điểm phải nằm trong khoảng 0-10. Vui lòng nhập lại!")
        except ValueError:
            print("Vui lòng nhập số hợp lệ!")
    sv = SinhVien(ho_ten, ma_sv, diem_toan, diem_ly, diem_hoa)
    print(sv.thong_tin())
