gia_ve = 100000 

ten = input("Nhập tên khách hàng: ")
tuoi = int(input("Nhập tuổi khách hàng: "))

giam_gia = 0.2 if tuoi < 18 or tuoi > 60 else 0
tien_ve = gia_ve * (1 - giam_gia)

print(f"Khách hàng: {ten}")
if giam_gia > 0:
    print("Vé giảm giá 20%")
print(f"Giá vé: {tien_ve:,.0f} VND")