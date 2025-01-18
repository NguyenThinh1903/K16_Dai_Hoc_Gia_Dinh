khach_hang = {}
while True:
  ten = input("Nhập tên khách hàng (hoặc 'x' để kết thúc): ")
  if ten == 'x':
    break
  tien = float(input(f"Nhập số tiền {ten} đã chi tiêu: "))
  khach_hang[ten] = tien

xep_hang = sorted(khach_hang.items(), key=lambda item: item[1], reverse=True)

print("Xếp hạng khách hàng:")
for ten, tien in xep_hang:
  print(f"+ {ten}: {tien:,.0f} VND")