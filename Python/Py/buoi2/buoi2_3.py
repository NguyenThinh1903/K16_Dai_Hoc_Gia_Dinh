a = float(input("Nhập số thứ nhất: "))
b = float(input("Nhập số thứ hai: "))
c = float(input("Nhập số thứ ba: "))

#print("Số lớn nhất là:", max(a, b, c))

if a >= b and a >= c:
    print("Số lớn nhất là:", a)
elif b >= a and b >= c:
    print("Số lớn nhất là:", b)
else:
    print("Số lớn nhất là:", c)
