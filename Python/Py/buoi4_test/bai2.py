import re

def kiem_tra_mat_khau(mk):
  return len(mk) >= 8 and re.search("[a-z]", mk) and re.search("[A-Z]", mk) and re.search("[0-9]", mk) and re.search("[!@#$%^&*()_+{}\[\]:;<>,.?~\\-]", mk)

mk = input("Nhập mật khẩu: ")
print("Mật khẩu mạnh" if kiem_tra_mat_khau(mk) else "Mật khẩu yếu")