menu = {"Phở": 50000, "Bún Chả": 60000}
tong_tien = 0

for mon, gia in menu.items():
  print(f"+ {mon} - {gia:,.0f} VND")

while True:
  chon = input("Chọn món (nhập tên món, hoặc 'x' để kết thúc): ")
  if chon == 'x':
    break
  if chon in menu:
    so_luong = int(input(f"Nhập số lượng {chon}: "))
    tong_tien += menu[chon] * so_luong
  else:
    print("Món không hợp lệ!")

print(f"Tổng tiền thanh toán: {tong_tien:,.0f} VND")