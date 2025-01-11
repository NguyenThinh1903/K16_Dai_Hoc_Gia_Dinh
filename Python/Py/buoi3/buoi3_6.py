n = int(input("nhap so"))
s = 0
if n % 2 == 0:
    for x in range(2, n + 1, 2):
        if x == 10:
            continue  
        s = s + x
elif n % 2 != 0:
    for x in range(1, n + 1, 2):
        if x == 11:
            continue 
        s = s + x
print("tong s=", s)

