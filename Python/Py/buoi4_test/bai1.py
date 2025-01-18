def tinh_luong(gio_lam, luong_gio):
    luong = min(gio_lam, 40)* luong_gio + max (0, gio_lam - 40) * luong_gio * 2
    return luong
gio_lam = float(input("số giờ làm: "))
luong_gio = float(input("lương theo giờ: "))
print(f"Luong: {tinh_luong(gio_lam, luong_gio):,.0f} VND")