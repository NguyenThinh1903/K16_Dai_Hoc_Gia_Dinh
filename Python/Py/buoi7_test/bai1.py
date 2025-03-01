# Bài 1: HỆ THỐNG DỰ ĐOÁN NGUY CƠ MẮC BỆNH PHỔI 
# Hãy xây dựng một chương trình Python áp dụng lập trình hướng đối tượng để mô phỏng 
# một hệ thống dự đoán nguy cơ mắc bệnh phổi dựa trên các yếu tố sức khỏe và lối sống. 
# Chương trình sẽ sử dụng các thông tin đầu vào như độ tuổi, tiền sử hút thuốc, môi trường 
# sống, tiền sử bệnh hô hấp và triệu chứng hiện tại để đưa ra dự đoán. 
# Hệ thống sẽ bao gồm một lớp đại diện cho mô hình dự đoán, với các phương thức cho 
# phép nhập thông tin sức khỏe, phân tích nguy cơ bằng công thức đánh giá hoặc thuật 
# toán học máy, hiển thị kết quả đánh giá nguy cơ và đưa ra khuyến nghị phòng ngừa.

class DiseaseRiskPrediction:
    def __init__(self):
        self.weights = {
            'age': 0.8,
            'smoking': 1.5,
            'environment': 1.2,
            'health_history': 1.8,
            'symptoms': 2.0
        }

    def input_health_info(self):
        age = float(input("Nhập độ tuổi: "))
        smoking = int(input("Nhập mức độ hút thuốc (0: Không hút, 5: Hút ít, 10: Hút nhiều): "))
        environment = int(input("Nhập mức độ ô nhiễm môi trường (0-10): "))
        health_history = int(input("Nhập tiền sử bệnh hô hấp (0: Không có, 10: Có bệnh hô hấp trước đây hoặc bệnh di truyền): "))
        symptoms = int(input("Nhập số triệu chứng hiện tại (0-10): "))

        return age, smoking, environment, health_history, symptoms

    def calculate_risk(self, age, smoking, environment, health_history, symptoms):
        A = age / 10 if age <= 50 else 10
        S = smoking
        E = environment
        H = health_history
        F = symptoms

        R = (self.weights['age'] * A) + (self.weights['smoking'] * S) + \
            (self.weights['environment'] * E) + (self.weights['health_history'] * H) + \
            (self.weights['symptoms'] * F)

        return R

    def display_result(self, R):
        if R < 10:
            print(f"Nguy cơ thấp ")
        elif 10 <= R < 20:
            print(f"Nguy cơ trung bình ")
        else:
            print(f"Nguy cơ cao ")

    def predict_disease_risk(self):
        age, smoking, environment, health_history, symptoms = self.input_health_info()
        R = self.calculate_risk(age, smoking, environment, health_history, symptoms)
        self.display_result(R)

if __name__ == "__main__":
    disease_risk_prediction = DiseaseRiskPrediction()
    disease_risk_prediction.predict_disease_risk()






















