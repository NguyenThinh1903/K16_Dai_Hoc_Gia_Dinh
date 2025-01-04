def so_ThanhChu(n):
    chu_so = ["không", "một", "hai", "ba", "bốn", "năm", "sáu", "bảy", "tám", "chín"]
    
    if n < 0:
        return "âm " + so_ThanhChu(abs(n))  # 
    elif n < 10:
        return chu_so[n]
    elif n < 20:
        return "mười " + chu_so[n % 10] if n % 10 != 0 else "mười"
    else:
        chuc = n // 10
        don_vi = n % 10
        if don_vi == 0:
            return chu_so[chuc] + " mươi"
        elif don_vi == 1:
            return chu_so[chuc] + " mươi mốt"
        else:
            return chu_so[chuc] + " mươi " + chu_so[don_vi]

try:
    n = int(input("Nhập một số (-99 đến 99): "))
    if -99 <= n <= 99:
        print(f"Số {n} đọc là: {so_ThanhChu(n)}")
    else:
        print("Vui lòng nhập số trong phạm vi từ -99 đến 99.")
except ValueError:
    print("Dữ liệu nhập vào không hợp lệ. Vui lòng nhập một số nguyên.")
