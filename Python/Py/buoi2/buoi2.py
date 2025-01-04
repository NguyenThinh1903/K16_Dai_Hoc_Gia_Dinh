import math

try:
        r = float(input("Nhập bán kính đường tròn r: "))
        cv = round(2 * math.pi * r, 2)
        dt = round(math.pi * (r ** 2), 2)
        print("Hình tròn bán kính {0} có chu vi là {1} và diện tích là {2}".format(r, cv, dt))

except ValueError:
    print("Dữ liệu nhập vào không hợp lệ. Vui lòng nhập một số thực.")
