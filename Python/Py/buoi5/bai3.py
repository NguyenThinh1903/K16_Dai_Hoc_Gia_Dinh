import random

def tao_list_ngau_nhien(n, min_val, max_val):

  return [random.randint(min_val, max_val) for _ in range(n)]

def la_so_nguyen_to(num):

  if num <= 1:
    return False  
  for i in range(2, int(num**0.5) + 1): 
    if num % i == 0:
      return False 
  return True      

def liet_ke_so_nguyen_to(my_list):
    so_nguyen_to = []
    for num in my_list:
        if la_so_nguyen_to(num):
            so_nguyen_to.append(num)
    return so_nguyen_to

# --- Chương trình chính ---
n = int(input("Nhập số lượng phần tử trong list: "))
min_value = int(input("Nhập giá trị nhỏ nhất: "))
max_value = int(input("Nhập giá trị lớn nhất: "))

#Tạo list ngẫu nhiên
random_list = tao_list_ngau_nhien(n, min_value, max_value)
print("List ngẫu nhiên:", random_list)

#Liệt kê và kiểm tra số nguyên tố
prime_numbers = liet_ke_so_nguyen_to(random_list)

if prime_numbers: #Kiểm tra list có rỗng không
    print("Các số nguyên tố trong list:", prime_numbers)
else:
    print("Không có số nguyên tố nào trong list.")