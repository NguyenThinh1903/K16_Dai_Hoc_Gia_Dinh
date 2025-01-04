try:
    month = int(input("Nhập tháng (1-12): "))
    
    if month in [1, 3, 5, 7, 8, 10, 12]:
        print(f"Tháng {month} có 31 ngày.")
    elif month in [4, 6, 9, 11]:
        print(f"Tháng {month} có 30 ngày.")
    elif month == 2:
        year = int(input("Nhập năm: "))
        
        # Kiểm tra năm nhuận
        if (year % 4 == 0 and year % 100 != 0) or (year % 400 == 0):
            print(f"Tháng 2 năm {year} có 29 ngày.")
        else:
            print(f"Tháng 2 năm {year} có 28 ngày.")
    else:
        print("Tháng không hợp lệ. Vui lòng nhập số từ 1 đến 12.")
        
except ValueError:
    print("Dữ liệu nhập vào không hợp lệ. Vui lòng nhập một số nguyên.")

'''
m = int(input("Nhập tháng (1-12): "))
    if m in [1, 3, 5, 7, 8, 10, 12]: print("31 ngày")
    elif m in [4, 6, 9, 11]: print("30 ngày")
    elif m == 2: print("29 ngày" if (y := int(input("Nhập năm: "))) % 4 == 0 and (y % 100 != 0 or y % 400 == 0) else "28 ngày")
    else: print("Tháng không hợp lệ")
'''