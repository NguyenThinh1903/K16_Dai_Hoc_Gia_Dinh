import math
from datetime import datetime

class ThucPham:
    def __init__(self):
        self.name = ""
        self.prod_date = None  # Ngày sản xuất
        self.exp_date = None   # Hạn sử dụng
        self.protein = 0       # Hàm lượng protein (g)
        self.fiber = 0         # Chất xơ (g)
        self.vitamin_c = 0     # Vitamin C (mg)
        self.energy = 0        # Năng lượng (calo)
        self.storage_temp = 0  # Nhiệt độ bảo quản (°C)

    def input_info(self):
        """Nhập thông tin thực phẩm từ người dùng"""
        self.name = input("Nhập tên thực phẩm: ")
        self.prod_date = datetime.strptime(input("Nhập ngày sản xuất (dd/mm/yyyy): "), "%d/%m/%Y")
        self.exp_date = datetime.strptime(input("Nhập hạn sử dụng (dd/mm/yyyy): "), "%d/%m/%Y")
        self.protein = float(input("Nhập hàm lượng protein (g): "))
        self.fiber = float(input("Nhập hàm lượng chất xơ (g): "))
        self.vitamin_c = float(input("Nhập hàm lượng vitamin C (mg): "))
        self.energy = float(input("Nhập tổng năng lượng (calo): "))
        self.storage_temp = float(input("Nhập nhiệt độ bảo quản (°C): "))

    def calculate_saf(self):
        """Tính chỉ số an toàn thực phẩm (SAF)"""
        current_date = datetime.now()
        S_d = (self.exp_date - self.prod_date).days  # Tổng số ngày sử dụng
        T_d = (current_date - self.prod_date).days   # Số ngày từ ngày sản xuất đến hiện tại
        SAF = ((S_d - T_d) / S_d) * 100

        print(f"Chỉ số an toàn (SAF): {SAF:.2f}%")
        if SAF >= 50:
            print("Thực phẩm an toàn để sử dụng.")
        elif 20 <= SAF < 50:
            print("Cảnh báo: Cần sử dụng sớm!")
        else:
            print("Nguy hiểm: Thực phẩm hết hạn hoặc không an toàn!")
        return SAF

    def calculate_nqi(self):
        """Tính chỉ số giá trị dinh dưỡng (NQI)"""
        NQI = (self.protein + self.fiber + self.vitamin_c) / self.energy
        print(f"Chỉ số dinh dưỡng (NQI): {NQI:.2f}")
        if NQI >= 0.5:
            print("Thực phẩm có giá trị dinh dưỡng cao.")
        elif 0.3 <= NQI < 0.5:
            print("Thực phẩm có giá trị dinh dưỡng trung bình.")
        else:
            print("Thực phẩm có giá trị dinh dưỡng thấp.")
        return NQI

    def optimal_storage(self):
        """Gợi ý bảo quản tối ưu"""
        T_ref = 4  # Nhiệt độ tiêu chuẩn 
        T_n = (self.exp_date - self.prod_date).days  # Thời gian bảo quản tiêu chuẩn
        k = 0.1  # Hệ số suy giảm do nhiệt độ 
        T_b = T_n * math.exp(-k * (self.storage_temp - T_ref))

        print(f"Thời gian bảo quản tối ưu ở {self.storage_temp}°C: {T_b:.1f} ngày")
        if self.storage_temp < T_ref:
            print("Bảo quản lạnh giúp kéo dài thời gian sử dụng.")
        elif self.storage_temp > T_ref:
            print("Nhiệt độ cao, thời gian bảo quản giảm nhanh. Nên làm lạnh.")
        return T_b

def main():
    print("HỆ THỐNG ĐÁNH GIÁ CHẤT LƯỢNG THỰC PHẨM")
    thuc_pham = ThucPham()  
    thuc_pham.input_info()
    print("\n--- ĐÁNH GIÁ THỰC PHẨM ---")
    thuc_pham.calculate_saf()
    thuc_pham.calculate_nqi()
    print("\n--- GỢI Ý BẢO QUẢN ---")
    thuc_pham.optimal_storage()

if __name__ == "__main__":
    main()