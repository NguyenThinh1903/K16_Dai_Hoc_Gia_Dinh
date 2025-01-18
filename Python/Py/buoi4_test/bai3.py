import random

so_bi_an = random.randint(1, 100)
print("Tôi đã chon số từ 1 - 100. Đoán xem")

while True:
    doan = int(input("Nhap so ban doan: "))
    print("qua thap! " if doan < so_bi_an else "qua cao!" if doan > so_bi_an else "dung!")
    if doan == so_bi_an:
        break