so_gio_lam = float(input("Nhập số giờ làm việc: "))
luong_theo_gio = float(input("Nhập lương theo giờ: "))

luong_co_ban = min(so_gio_lam, 40) * luong_theo_gio
luong_lam_them = max(0, so_gio_lam - 40) * luong_theo_gio * 2
tong_luong = luong_co_ban + luong_lam_them

print(f"Tiền lương: {tong_luong:,.0f} VND ", end="")
if so_gio_lam > 40:
  print(f"({luong_co_ban:,.0f} VND cho 40 giờ và {luong_lam_them:,.0f} VND cho {so_gio_lam - 40:.0f} giờ làm thêm).")
else:
    print("")