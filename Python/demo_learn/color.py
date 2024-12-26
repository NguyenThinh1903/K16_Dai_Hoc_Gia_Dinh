import cv2
import numpy as np

# Đọc ảnh
img1 = cv2.imread(r"D:\Downloads\1.jpg")
img2 = cv2.imread(r"D:\Downloads\2.jpg")
img3 = cv2.imread(r"D:\Downloads\3.jpg")

# Chuyển ảnh từ không gian màu BGR sang HSV
hsv1 = cv2.cvtColor(img1, cv2.COLOR_BGR2HSV)
hsv2 = cv2.cvtColor(img2, cv2.COLOR_BGR2HSV)
hsv3 = cv2.cvtColor(img3, cv2.COLOR_BGR2HSV)

# Hàm phân loại màu sắc dựa trên HSV
def classify_color(hsv_img):
    # Ngưỡng cho từng màu trong không gian HSV
    lower_red1 = np.array([0, 100, 100])
    upper_red1 = np.array([10, 255, 255])
    lower_red2 = np.array([170, 100, 100])
    upper_red2 = np.array([180, 255, 255])
    lower_yellow = np.array([20, 100, 100])
    upper_yellow = np.array([40, 255, 255])
    lower_green = np.array([40, 100, 100])
    upper_green = np.array([80, 255, 255])

    # Tạo các mặt nạ (mask) cho các màu
    mask_red1 = cv2.inRange(hsv_img, lower_red1, upper_red1)
    mask_red2 = cv2.inRange(hsv_img, lower_red2, upper_red2)
    mask_yellow = cv2.inRange(hsv_img, lower_yellow, upper_yellow)
    mask_green = cv2.inRange(hsv_img, lower_green, upper_green)

    # Tính trung bình của mỗi mask
    mean_red = np.mean(mask_red1) + np.mean(mask_red2)
    mean_yellow = np.mean(mask_yellow)
    mean_green = np.mean(mask_green)

    # Dựa vào giá trị trung bình để phân loại màu sắc
    if mean_red > mean_yellow and mean_red > mean_green:
        return "RED"
    elif mean_yellow > mean_red and mean_yellow > mean_green:
        return "YELLOW"
    else:
        return "GREEN"

# Áp dụng hàm phân loại cho từng ảnh
color1 = classify_color(hsv1)
color2 = classify_color(hsv2)
color3 = classify_color(hsv3)

# Cài đặt font và vị trí để hiển thị
font = cv2.FONT_HERSHEY_SIMPLEX
font_scale = 0.7
color = (0, 255, 0)  # Màu chữ là màu xanh lá
thickness = 2
position = (20, 20)

# Thêm tên màu vào ảnh
cv2.putText(img1, color1, position, font, font_scale, color, thickness)
cv2.putText(img2, color2, position, font, font_scale, color, thickness)
cv2.putText(img3, color3, position, font, font_scale, color, thickness)

# Hiển thị các ảnh
cv2.imshow("Image 1", img1)
cv2.imshow("Image 2", img2)
cv2.imshow("Image 3", img3)

cv2.waitKey(0)
cv2.destroyAllWindows()
