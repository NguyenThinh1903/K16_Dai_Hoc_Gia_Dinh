gioi = 0
kha = 0
tong = 0

while True:
  diem = input("Nhập điểm sinh viên (hoặc 'x' để kết thúc): ")
  if diem == 'x':
    break
  diem = float(diem)
  tong += 1

  if 8.0 <= diem <= 10.0:
    print("Loại Giỏi")
    gioi += 1
  elif 6.5 <= diem < 8.0:
    print("Loại Khá")
    kha += 1
  elif 5.0 <= diem < 6.5:
    print("Loại Trung bình")
  else:
    print("Loại Yếu")

ty_le = (gioi + kha) / tong * 100 if tong > 0 else 0
print(f"Tỷ lệ sinh viên đạt loại Giỏi hoặc Khá: {ty_le:.2f}%")