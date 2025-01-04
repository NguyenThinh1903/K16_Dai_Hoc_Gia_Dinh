from datetime import datetime, timedelta

def ngay_ke_sau(ngay, thang, nam):
    try:
        # Chuyển đổi ngày, tháng, năm thành đối tượng datetime
        ngay_hien_tai = datetime(nam, thang, ngay)
        
        # Tính ngày kế tiếp
        ngay_ke = ngay_hien_tai + timedelta(days=1)
        
        # Trả về ngày, tháng, năm của ngày kế tiếp
        return ngay_ke.day, ngay_ke.month, ngay_ke.year
    except ValueError:
        return "Ngày không hợp lệ. Vui lòng kiểm tra lại."

# Nhập ngày, tháng, năm
ngay = int(input("Nhập ngày: "))
thang = int(input("Nhập tháng: "))
nam = int(input("Nhập năm: "))

ngay_sau, thang_sau, nam_sau = ngay_ke_sau(ngay, thang, nam)
print(f"Ngày kế tiếp là: {ngay_sau}/{thang_sau}/{nam_sau}")
