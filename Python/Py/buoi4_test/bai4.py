gia_tri = float(input("nhap gia tri mua hang (VND): "))
giam_gia = 0.1 if gia_tri > 1000000 else 0
tien_thanh_toan = gia_tri * (1 - giam_gia)
print(f"so tien thanh toan: {tien_thanh_toan:,.0f} VND ({'Giam 10%' if giam_gia else ''})")