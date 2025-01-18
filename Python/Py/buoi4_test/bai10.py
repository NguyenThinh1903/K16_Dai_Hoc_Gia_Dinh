muc_tieu = float(input("Nhap muc tieu tiet kiem (VND): "))
tien_tiet_kiem_moi_ngay = float(input("Nhap so tien tiet kiem moi ngay (VND): "))

so_ngay = ( muc_tieu + tien_tiet_kiem_moi_ngay - 1) // tien_tiet_kiem_moi_ngay

print(f"so ngay can tiet kiem: {int(so_ngay)} ngay")