trong_luong_trai_dat = float(input("Nhập trọng lượng hiện tại của bạn trên Trái Đất (kg): "))
ty_le_trong_luong_mat_trang = 0.125
so_nam = 15

for nam in range(1, so_nam + 1):
    trong_luong_trai_dat_hien_tai = trong_luong_trai_dat + nam
    trong_luong_mat_trang = trong_luong_trai_dat_hien_tai * ty_le_trong_luong_mat_trang
    print(f"Năm thứ {nam}: Trọng lượng trên Mặt Trăng = {trong_luong_mat_trang:.2f} kg")
trong_luong_cuoi_cung_tren_mat_trang = (trong_luong_trai_dat + so_nam) * ty_le_trong_luong_mat_trang
print(f"\nTrọng lượng trên Mặt Trăng sau 15 năm: {trong_luong_cuoi_cung_tren_mat_trang:.2f} kg")