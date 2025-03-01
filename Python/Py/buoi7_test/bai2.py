class HanhTinh:
    def __init__(self, ten, trong_luong, ban_kinh, khoang_cach):
        self.ten = ten           
        self.trong_luong = trong_luong  
        self.ban_kinh = ban_kinh       
        self.khoang_cach = khoang_cach  

    def cap_nhat(self, trong_luong=None, ban_kinh=None, khoang_cach=None):
        if trong_luong:
            self.trong_luong = trong_luong
        if ban_kinh:
            self.ban_kinh = ban_kinh
        if khoang_cach:
            self.khoang_cach = khoang_cach

    def hien_thi(self):
        print(f"Hành tinh: {self.ten}")
        print(f"Trọng lượng: {self.trong_luong} kg")
        print(f"Bán kính: {self.ban_kinh} m")
        print(f"Khoảng cách tới Mặt Trời: {self.khoang_cach} m")

if __name__ == "__main__":
    trai_dat = HanhTinh("Trái Đất", 5.972e24, 6371000, 1.496e11)
    sao_hoa = HanhTinh("Sao Hỏa", 6.39e23, 3389000, 2.279e11)

    print("\nThông tin Trái Đất:")
    trai_dat.hien_thi()

    print("\nThông tin Sao Hỏa:")
    sao_hoa.hien_thi()

    # Cập nhật thông tin
    trai_dat.cap_nhat(khoang_cach=1.5e11)
    sao_hoa.cap_nhat(ban_kinh=3390000)

    print("\nThông tin Trái Đất sau cập nhật:")
    trai_dat.hien_thi()

    print("\nThông tin Sao Hỏa sau cập nhật:")
    sao_hoa.hien_thi()