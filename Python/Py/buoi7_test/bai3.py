class StockPredictor:
    def __init__(self):
        self.days = []  
        self.prices = []  
        self.a = 0  
        self.b = 0  

    def input_data(self):
        """Nhập dữ liệu lịch sử giá cổ phiếu"""
        n = int(input("Nhập số ngày giao dịch: "))
        for i in range(n):
            day = i + 1  
            price = float(input(f"Nhập giá cổ phiếu ngày {day}: "))
            self.days.append(day)
            self.prices.append(price)

    def calculate_regression(self):
        """Tính toán hồi quy tuyến tính: P_t = a * t + b"""
        n = len(self.days)
        if n == 0:
            print("Chưa có dữ liệu để tính toán!")
            return

        
        sum_t = sum(self.days)
        sum_p = sum(self.prices)
        sum_tp = sum(t * p for t, p in zip(self.days, self.prices))
        sum_t2 = sum(t * t for t in self.days)

        
        self.a = (n * sum_tp - sum_t * sum_p) / (n * sum_t2 - sum_t ** 2)
        self.b = (sum_p - self.a * sum_t) / n

        print(f"Phương trình hồi quy: P_t = {self.a:.2f} * t + {self.b:.2f}")

    def predict_price(self, future_day):
        """Dự đoán giá cổ phiếu cho ngày tương lai"""
        predicted_price = self.a * future_day + self.b
        print(f"Giá dự đoán cho ngày {future_day}: {predicted_price:.2f}")
        return predicted_price

    def investment_suggestion(self):
        """Đưa ra gợi ý đầu tư dựa trên xu hướng"""
        if self.a > 0.01: 
            print("Xu hướng tăng → Có thể mua vào.")
        elif self.a < -0.01:
            print("Xu hướng giảm → Cân nhắc bán ra.")
        else:
            print("Giá ít biến động → Cần theo dõi thêm.")

def main():
    print("HỆ THỐNG DỰ ĐOÁN GIÁ CỔ PHIẾU")
    stock = StockPredictor()
    stock.input_data()
    stock.calculate_regression()
    
    future_day = int(input("Nhập ngày muốn dự đoán (tương lai): "))
    stock.predict_price(future_day)
    stock.investment_suggestion()

if __name__ == "__main__":
    main()