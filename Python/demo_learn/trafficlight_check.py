import cv2
import imutils
import numpy as np
import time
import psutil

# path video trafficlightt
video_path = r"D:\Documents\K16_Dai_Hoc_Gia_Dinh\Python\demo_learn\public\trafficlight_check.mp4"

cap = cv2.VideoCapture(video_path)

if not cap.isOpened():
    print("Không thể mở video")
    exit()

red_lower = np.array([0, 120, 70])
red_upper = np.array([10, 255, 255])
red_lower2 = np.array([170, 120, 70])
red_upper2 = np.array([180, 255, 255])

yellow_lower = np.array([15, 100, 100])
yellow_upper = np.array([35, 255, 255])

green_lower = np.array([40, 50, 50])
green_upper = np.array([90, 255, 255])

frame_count = 0
detected_frames = 0
start_time = time.time()

traffic_light_state = "Unknown"

while True:
    ret, frame = cap.read()
    if not ret:
        print("Không thể đọc khung hình từ video")
        break

    frame_count += 1

    frame = imutils.resize(frame, width=800)

    hsv = cv2.cvtColor(frame, cv2.COLOR_BGR2HSV)

    mask_red1 = cv2.inRange(hsv, red_lower, red_upper)
    mask_red2 = cv2.inRange(hsv, red_lower2, red_upper2)
    mask_red = cv2.bitwise_or(mask_red1, mask_red2)

    mask_yellow = cv2.inRange(hsv, yellow_lower, yellow_upper)

    mask_green = cv2.inRange(hsv, green_lower, green_upper)
    
    detected = False
    for mask, color, label, state in [
        (mask_red, (0, 0, 255), "Red", "Stop"),
        (mask_yellow, (0, 255, 255), "Yellow", "Slow Run"),
        (mask_green, (0, 255, 0), "Green", "Run")
    ]:
        contours, _ = cv2.findContours(mask, cv2.RETR_TREE, cv2.CHAIN_APPROX_SIMPLE)
        for contour in contours:
            area = cv2.contourArea(contour)
            if area > 500:  # Lọc vùng nhiễu nhỏ
                x, y, w, h = cv2.boundingRect(contour)
                cv2.rectangle(frame, (x, y), (x + w, y + h), color, 2)
                cv2.putText(frame, label, (x, y - 10), cv2.FONT_HERSHEY_SIMPLEX, 0.6, color, 2)
                traffic_light_state = state
                detected = True

    if not detected:
        traffic_light_state = "unknown"

    if detected:
        detected_frames += 1

    cv2.putText(frame, f"CCondition: {traffic_light_state}", (50, 140), cv2.FONT_HERSHEY_SIMPLEX, 0.8, (255, 255, 255), 2)

    elapsed_time = time.time() - start_time
    fps = frame_count / elapsed_time if elapsed_time > 0 else 0
    cpu_usage = psutil.cpu_percent()
    memory_usage = psutil.virtual_memory().percent

    cv2.putText(frame, f"FPS: {fps:.2f}", (50, 50), cv2.FONT_HERSHEY_SIMPLEX, 0.6, (255, 255, 255), 2)
    cv2.putText(frame, f"CPU: {cpu_usage}%", (50, 80), cv2.FONT_HERSHEY_SIMPLEX, 0.6, (255, 255, 255), 2)
    cv2.putText(frame, f"Memory: {memory_usage}%", (50, 110), cv2.FONT_HERSHEY_SIMPLEX, 0.6, (255, 255, 255), 2)

    cv2.imshow('Nhận Diện Đèn Giao Thông', frame)

    if cv2.waitKey(1) & 0xFF == ord('q'):
        break

accuracy = (detected_frames / frame_count) * 100 if frame_count > 0 else 0

print("\n===== Đánh Giá Hiệu Suất =====")
print(f"❇️ Tổng số khung hình: {frame_count}")
print(f"✅ Số khung hình phát hiện: {detected_frames}")
print(f"🎯 Độ chính xác: {accuracy:.2f}%")
print(f"⚡ Tốc độ khung hình trung bình (FPS): {fps:.2f}")
print(f"🖥️ Sử dụng CPU trung bình: {cpu_usage}%")
print(f"💾 Sử dụng bộ nhớ trung bình: {memory_usage}%")

with open("traffic_light_performance_log.txt", "a") as log_file:
    log_file.write(f"Frames: {frame_count}, Detected: {detected_frames}, Accuracy: {accuracy:.2f}%, FPS: {fps:.2f}, CPU: {cpu_usage}%, Memory: {memory_usage}%\n")

cap.release()
cv2.destroyAllWindows()