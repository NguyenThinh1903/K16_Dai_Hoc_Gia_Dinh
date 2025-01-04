try:
    toan = float(input("Nhập điểm môn Toán: "))
    ly = float(input("Nhập điểm môn Lý: "))
    hoa = float(input("Nhập điểm môn Hóa: "))
    diem_tb = round((toan + ly + hoa) / 3, 2)
    print("Điểm trung bình của học sinh:", diem_tb)

except ValueError:
    print("Dữ liệu nhập vào không hợp lệ. Vui lòng nhập số thực.")
