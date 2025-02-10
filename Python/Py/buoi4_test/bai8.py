ma_pin_dung = "1234"
so_lan_thu = 0

while so_lan_thu < 3:
  ma_pin = input("Nhập mã PIN: ")
  if ma_pin == ma_pin_dung:
    print("Đăng nhập thành công!")
    break
  else:
    so_lan_thu += 1
    print("Sai mã PIN!")
    if so_lan_thu < 3:
        print(f"Bạn còn {3-so_lan_thu} lần thử")

if so_lan_thu == 3:
  print("Thẻ bị khóa!")
  