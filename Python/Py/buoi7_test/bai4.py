import math
from datetime import datetime

class ThucPham:
    def __init__(self):
        self.name = "Sữa"     
        self.prod_date = None  
        self.exp_date = None   
        self.protein = 2.5    
        self.fiber = 0         
        self.vitamin_c = 1.0   
        self.energy = 60       
        self.storage_temp = 4  

    def input_info(self):
        self.prod_date = datetime.strptime(input("Nhập ngày sản xuất (dd/mm/yyyy): "), "%d/%m/%Y")
        self.exp_date = datetime.strptime(input("Nhập hạn sử dụng (dd/mm/yyyy): "), "%d/%m/%Y")

    def calculate_saf(self):
        current_date = datetime.now()
        S_d = (self.exp_date - self.prod_date).days  # Tổng số ngày sử dụng
        T_d = (current_date - self.prod_date).days   # Số ngày từ ngày sản xuất đến hiện tại
        SAF = ((S_d - T_d) / S_d) * 100

        # So sánh với ngày hiện tại
        days_used = T_d  # Số ngày đã sử dụng
        days_left = S_d - T_d  # Số ngày còn lại đến hạn sử dụng
        
        print(f"Ngày hiện tại: {current_date.strftime('%d/%m/%Y')}")
        print(f"Đã sử dụng: {days_used} ngày kể từ ngày sản xuất ({self.prod_date.strftime('%d/%m/%Y')})")
        if days_left >= 0:
            print(f"Còn lại: {days_left} ngày đến ngày hết hạn ({self.exp_date.strftime('%d/%m/%Y')})")
        else:
            print(f"Đã quá hạn: {abs(days_left)} ngày kể từ ngày hết hạn ({self.exp_date.strftime('%d/%m/%Y')})")
        
        print(f"Chỉ số an toàn (SAF): {SAF:.2f}%")
        if SAF >= 50:
            print("Thực phẩm an toàn để sử dụng.")
        elif 20 <= SAF < 50:
            print("Cảnh báo: Cần sử dụng sớm!")
        else:
            print("Nguy hiểm: Thực phẩm hết hạn hoặc không an toàn!")
        return SAF

    def calculate_nqi(self):
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
        T_ref = 4   
        T_n = (self.exp_date - self.prod_date).days  
        k = 0.1  
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
    print(f"\nThông tin thực phẩm: {thuc_pham.name}")
    print(f"Protein: {thuc_pham.protein}g, Chất xơ: {thuc_pham.fiber}g, Vitamin C: {thuc_pham.vitamin_c}mg, Năng lượng: {thuc_pham.energy} calo")
    print(f"Nhiệt độ bảo quản: {thuc_pham.storage_temp}°C")
    print("\n--- ĐÁNH GIÁ THỰC PHẨM ---")
    thuc_pham.calculate_saf()
    thuc_pham.calculate_nqi()
    print("\n--- GỢI Ý BẢO QUẢN ---")
    thuc_pham.optimal_storage()

if __name__ == "__main__":
    main()