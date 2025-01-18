tieu_thu = float(input("Nhập tiêu thụ điện (kWh): "))

if tieu_thu <= 50:
  tien_dien = tieu_thu * 1500
elif tieu_thu <= 100:
  tien_dien = 50 * 1500 + (tieu_thu - 50) * 2000
else:
  tien_dien = 50 * 1500 + 50 * 2000 + (tieu_thu - 100) * 2500

print(f"Số tiền thanh toán: {tien_dien:,.0f} VND")