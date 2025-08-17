import cv2
import numpy as np
import os
import time
import subprocess

# --- CẤU HÌNH ---

# 1. Điền ID thiết bị của bạn vào đây (lấy từ lệnh 'adb devices')
#    Đây là ID bạn thấy sau khi chạy adb connect thành công
DEVICE_ID = "192.168.1.166:38737" # <-- SỬA LẠI ID NÀY CHO ĐÚNG HOẶC ĐỂ TRỐNG NẾU CHỈ CÓ 1 THIẾT BỊ

# 2. Cung cấp đường dẫn đầy đủ đến adb.exe
#    Dùng dấu \\ hoặc /
ADB_PATH = "C:\\platform-tools\\adb.exe"

# 3. Các cấu hình khác
SIMILARITY_THRESHOLD = 0.86
AD_WATCH_DELAY = 30 
SCAN_INTERVAL = 2
ROI_TOP_PERCENTAGE = 0.3 

HIGH_PRIORITY_FOLDER = "high_priority"
LOW_PRIORITY_FOLDER = "low_priority"

# --- CÁC HÀM CHỨC NĂNG (Đã được sửa để dùng ADB_PATH) ---
def capture_screen():
    # Bây giờ lệnh if DEVICE_ID sẽ hoạt động vì biến đã được khai báo
    if DEVICE_ID: 
        command = [ADB_PATH, "-s", DEVICE_ID, "exec-out", "screencap", "-p"]
    else: 
        command = [ADB_PATH, "exec-out", "screencap", "-p"]
    try:
        result = subprocess.run(command, capture_output=True, check=True, timeout=10)
        img = cv2.imdecode(np.frombuffer(result.stdout, np.uint8), cv2.IMREAD_COLOR)
        return img
    except Exception as e:
        print(f"Lỗi trong capture_screen: {e}"); return None

def find_image(screen_image_gray, template_path):
    template_gray = cv2.imread(template_path, cv2.IMREAD_GRAYSCALE)
    if template_gray is None: return 0, None
    result = cv2.matchTemplate(screen_image_gray, template_gray, cv2.TM_CCOEFF_NORMED)
    _, max_val, _, max_loc = cv2.minMaxLoc(result)
    return max_val, max_loc

def click_at(coords, template_path):
    template_gray = cv2.imread(template_path, cv2.IMREAD_GRAYSCALE)
    h, w = template_gray.shape
    center_x = coords[0] + w // 2
    center_y = coords[1] + h // 2
    print(f"==> ĐÃ TÌM THẤY '{os.path.basename(template_path)}'. Nhấn tại ({center_x}, {center_y})!")
    if DEVICE_ID: 
        command = f"{ADB_PATH} -s {DEVICE_ID} shell input tap {center_x} {center_y}"
    else: 
        command = f"{ADB_PATH} shell input tap {center_x} {center_y}"
    os.system(command)

def get_template_files(folder):
    files = []
    if not os.path.isdir(folder): return []
    for f in os.listdir(folder):
        if f.lower().endswith(('.png', '.jpg', '.jpeg')): files.append(os.path.join(folder, f))
    return files

def find_best_match_in_folder(screen_gray, templates_list, folder_name, use_roi=False):
    best_match_value = -1; best_match_location = None; best_match_template_path = None
    if not templates_list: return None
    target_screen = screen_gray
    roi_offset_y = 0
    if use_roi:
        height, _ = screen_gray.shape
        roi_height = int(height * ROI_TOP_PERCENTAGE)
        target_screen = screen_gray[0:roi_height, :]
        print(f"[{folder_name}] Quét trong vùng ROI...")
    for template_path in templates_list:
        match_value, match_location = find_image(target_screen, template_path)
        if match_value > best_match_value:
            best_match_value = match_value
            best_match_location = match_location
            best_match_template_path = template_path
    if best_match_template_path:
        print(f"[{folder_name}] Giống nhất: {os.path.basename(best_match_template_path)} ({best_match_value:.2f})")
    if best_match_value >= SIMILARITY_THRESHOLD:
        final_location = (best_match_location[0], best_match_location[1] + roi_offset_y)
        return {"path": best_match_template_path, "location": final_location}
    return None

# --- VÒNG LẶP CHÍNH ---
def main():
    print("--- Bắt đầu Bot (ROI) ---")
    high_prio_templates = get_template_files(HIGH_PRIORITY_FOLDER)
    low_prio_templates = get_template_files(LOW_PRIORITY_FOLDER)
    bot_state = "TIM_VIEC"
    print(f"Thời gian chờ quảng cáo: {AD_WATCH_DELAY} giây. Ngưỡng nhận diện: {SIMILARITY_THRESHOLD*100}%.")
    while True:
        print("\n" + "="*40 + f"\nTrạng thái: {bot_state}")
        screen = capture_screen()
        if screen is None: time.sleep(5); continue
        screen_gray = cv2.cvtColor(screen, cv2.COLOR_BGR2GRAY)
        if bot_state == "TIM_NUT_DONG":
            best_match = find_best_match_in_folder(screen_gray, high_prio_templates, HIGH_PRIORITY_FOLDER, use_roi=True)
            if best_match:
                click_at(best_match["location"], best_match["path"])
                time.sleep(2)
                print("==> Đã đóng QC. Chuyển về trạng thái TIM_VIEC.")
                bot_state = "TIM_VIEC"
            else:
                best_match_low = find_best_match_in_folder(screen_gray, low_prio_templates, LOW_PRIORITY_FOLDER)
                if best_match_low:
                    print("!!! Tự sửa sai: Tìm nút đóng nhưng thấy nút xem QC. Chuyển trạng thái.")
                    bot_state = "TIM_VIEC"
                else:
                    print("Chưa tìm thấy nút đóng. Chờ...")
        elif bot_state == "TIM_VIEC":
            action_taken = False
            best_match_high = find_best_match_in_folder(screen_gray, high_prio_templates, HIGH_PRIORITY_FOLDER, use_roi=True)
            if best_match_high:
                click_at(best_match_high["location"], best_match_high["path"])
                time.sleep(2)
                action_taken = True
            if not action_taken:
                best_match_low = find_best_match_in_folder(screen_gray, low_prio_templates, LOW_PRIORITY_FOLDER)
                if best_match_low:
                    click_at(best_match_low["location"], best_match_low["path"])
                    print(f"==> Đã nhấn xem QC. Bắt đầu chờ cố định {AD_WATCH_DELAY} giây...")
                    time.sleep(AD_WATCH_DELAY)
                    print("==> Chờ cố định kết thúc. Chuyển sang trạng thái TIM_NUT_DONG.")
                    bot_state = "TIM_NUT_DONG"
                    action_taken = True
            if not action_taken:
                print("Không tìm thấy việc gì để làm. Chờ...")
        time.sleep(SCAN_INTERVAL) 

if __name__ == "__main__":
    try: main()
    except KeyboardInterrupt: print("\n--- Bot đã dừng. ---")