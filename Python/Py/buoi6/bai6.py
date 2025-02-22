class TaiKhoan:
    def __init__(self, so_tai_khoan: str, chu_tai_khoan: str, so_du: float = 0.0):
        self.so_tai_khoan = so_tai_khoan
        self.chu_tai_khoan = chu_tai_khoan
        self.so_du = so_du

    def nap_tien(self, so_tien: float) -> bool:
        if so_tien > 0:
            self.so_du += so_tien
            print(f"Đã nạp {so_tien} VND vào tài khoản {self.so_tai_khoan}.")
            return True
        else:
            print("Số tiền nạp phải lớn hơn 0!")
            return False

    def rut_tien(self, so_tien: float) -> bool:
        if so_tien <= 0:
            print("Số tiền rút phải lớn hơn 0!")
            return False
        elif so_tien > self.so_du:
            print("Số dư không đủ để rút tiền!")
            return False
        else:
            self.so_du -= so_tien
            print(f"Đã rút {so_tien} VND từ tài khoản {self.so_tai_khoan}.")
            return True

    def hien_thi_so_du(self) -> str:
        return f"Tài khoản {self.so_tai_khoan} - Chủ tài khoản: {self.chu_tai_khoan}, Số dư: {self.so_du} VND"

if __name__ == "__main__":
    # Nhập thông tin tài khoản từ bàn phím
    print("Nhập thông tin tài khoản:")
    while True:
        so_tai_khoan = input("Nhập số tài khoản: ").strip()
        if not so_tai_khoan:
            print("Số tài khoản không được để trống. Vui lòng nhập lại!")
            continue
        break

    while True:
        chu_tai_khoan = input("Nhập tên chủ tài khoản: ").strip()
        if not chu_tai_khoan:
            print("Tên chủ tài khoản không được để trống. Vui lòng nhập lại!")
            continue
        break

    while True:
        try:
            so_du = float(input("Nhập số dư ban đầu (VND): "))
            if so_du < 0:
                print("Số dư ban đầu phải là số không âm. Vui lòng nhập lại!")
                continue
            break
        except ValueError:
            print("Vui lòng nhập số hợp lệ cho số dư!")

    # Tạo tài khoản
    tk = TaiKhoan(so_tai_khoan, chu_tai_khoan, so_du)
    print("\nTài khoản đã được tạo:")
    print(tk.hien_thi_so_du())

    # Thực hiện các giao dịch
    while True:
        print("\nChọn thao tác:")
        print("1. Nạp tiền")
        print("2. Rút tiền")
        print("3. Hiển thị số dư")
        print("4. Thoát")
        lua_chon = input("Nhập lựa chọn (1-4): ")

        if lua_chon == "1":
            while True:
                try:
                    so_tien = float(input("Nhập số tiền cần nạp (VND): "))
                    tk.nap_tien(so_tien)
                    break
                except ValueError:
                    print("Vui lòng nhập số hợp lệ!")

        elif lua_chon == "2":
            while True:
                try:
                    so_tien = float(input("Nhập số tiền cần rút (VND): "))
                    tk.rut_tien(so_tien)
                    break
                except ValueError:
                    print("Vui lòng nhập số hợp lệ!")

        elif lua_chon == "3":
            print(tk.hien_thi_so_du())

        elif lua_chon == "4":
            print("Cảm ơn bạn đã sử dụng dịch vụ!")
            break

        else:
            print("Lựa chọn không hợp lệ. Vui lòng chọn lại!")