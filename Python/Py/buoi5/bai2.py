def tinh_bmi():
    """
    Chương trình tính chỉ số BMI (Body Mass Index) và đưa ra đánh giá.
    """
    while True:
        try:
            can_nang = float(input("Nhập cân nặng của bạn (kg): "))
            if can_nang <= 0:
                print("Cân nặng phải lớn hơn 0. Vui lòng nhập lại.")
                continue

            chieu_cao = float(input("Nhập chiều cao của bạn (m): ")) 
            if chieu_cao <= 0:
                print("Chiều cao phải lớn hơn 0. Vui lòng nhập lại.")
                continue
            
            break 
        except ValueError:
            print("Lỗi: Cân nặng và chiều cao phải là số. Vui lòng nhập lại.")

    bmi = can_nang / (chieu_cao * chieu_cao)  
    print(f"Chỉ số BMI của bạn là: {bmi:.2f}")

    if bmi < 18.5:
        print("Bạn gầy. Nguy cơ phát triển bệnh thấp")
    elif 18.5 <= bmi < 24.9:
        print("Bạn bình thường. Nguy cơ phát triển bệnh trung bình")
    elif 25 <= bmi < 29.9:
        print("Bạn béo phì . Nguy cơ phát triển bệnh cao")
    elif 30 <= bmi < 34.9:
        print("Bạn béo phì cấp dộ 1. Nguy cơ phát triển bệnh cao")
    elif 35 <= bmi < 39.9:
        print("Bạn béo phì cấp dộ 2. Nguy cơ phát triển bệnh rất cao")
    else:
        print("Bạn béo phì. Nguy hiểm")

tinh_bmi()