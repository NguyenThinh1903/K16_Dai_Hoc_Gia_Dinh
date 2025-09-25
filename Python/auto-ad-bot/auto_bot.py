import cv2
import numpy as np
import os
import time
import subprocess
import logging
from settings import DEVICE_ID, ADB_PATH, SIMILARITY_THRESHOLD, AD_WATCH_DELAY, SCAN_INTERVAL, ROI_TOP_PERCENTAGE, HIGH_PRIORITY_FOLDER, LOW_PRIORITY_FOLDER

# --- CẤU HÌNH ---
# Configure logging
logging.basicConfig(level=logging.INFO, format='%(asctime)s - %(levelname)s - %(message)s')
logger = logging.getLogger(__name__)

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
        logger.error(f"Lỗi trong capture_screen: {e}"); return None

# Cache for template images
template_cache = {}

def get_template_image(template_path):
    """Get template image from cache or read from disk if not cached."""
    if template_path not in template_cache:
        template_cache[template_path] = cv2.imread(template_path, cv2.IMREAD_GRAYSCALE)
    return template_cache[template_path]

def find_image(screen_image_gray, template_path):
    template_gray = get_template_image(template_path)
    if template_gray is None: return 0, None
    result = cv2.matchTemplate(screen_image_gray, template_gray, cv2.TM_CCOEFF_NORMED)
    _, max_val, _, max_loc = cv2.minMaxLoc(result)
    return max_val, max_loc

def click_at(coords, template_path):
    template_gray = get_template_image(template_path)
    h, w = template_gray.shape
    center_x = coords[0] + w // 2
    center_y = coords[1] + h // 2
    logger.info(f"==> ĐÃ TÌM THẤY '{os.path.basename(template_path)}'. Nhấn tại ({center_x}, {center_y})!")
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
        logger.info(f"[{folder_name}] Quét trong vùng ROI...")
    for template_path in templates_list:
        match_value, match_location = find_image(target_screen, template_path)
        if match_value > best_match_value:
            best_match_value = match_value
            best_match_location = match_location
            best_match_template_path = template_path
    if best_match_template_path:
        logger.info(f"[{folder_name}] Giống nhất: {os.path.basename(best_match_template_path)} ({best_match_value:.2f})")
    # Sử dụng ngưỡng động: 80% của giá trị match cao nhất, nhưng không thấp hơn 0.7
    dynamic_threshold = max(0.7, best_match_value * 0.8)
    if best_match_value >= dynamic_threshold:
        final_location = (best_match_location[0], best_match_location[1] + roi_offset_y)
        return {"path": best_match_template_path, "location": final_location}
    return None

# --- VÒNG LẶP CHÍNH ---
def main():
    logger.info("--- Bắt đầu Bot (Cải tiến) ---")
    high_prio_templates = get_template_files(HIGH_PRIORITY_FOLDER)
    low_prio_templates = get_template_files(LOW_PRIORITY_FOLDER)
    bot_state = "TIM_VIEC"
    logger.info(f"Thời gian chờ quảng cáo: {AD_WATCH_DELAY} giây. Sử dụng ngưỡng động.")
    
    # Adaptive sleep variables
    current_sleep_interval = SCAN_INTERVAL
    max_sleep_interval = 10  # Maximum sleep interval
    no_action_count = 0
    reset_action_threshold = 3  # Reset sleep interval after this many actions
    
    while True:
        logger.info("\n" + "="*40 + f"\nTrạng thái: {bot_state}")
        screen = capture_screen()
        if screen is None: 
            time.sleep(5)
            continue
        screen_gray = cv2.cvtColor(screen, cv2.COLOR_BGR2GRAY)
        action_taken = False
        
        if bot_state == "TIM_NUT_DONG":
            # Quét toàn bộ màn hình để tìm nút đóng
            best_match = find_best_match_in_folder(screen_gray, high_prio_templates, HIGH_PRIORITY_FOLDER, use_roi=False)
            if best_match:
                click_at(best_match["location"], best_match["path"])
                time.sleep(2)
                logger.info("==> Đã đóng QC. Chuyển về trạng thái TIM_VIEC.")
                bot_state = "TIM_VIEC"
                action_taken = True
            else:
                # Nếu không tìm thấy nút đóng, quay lại trạng thái TIM_VIEC
                logger.info("Không tìm thấy nút đóng. Quay lại trạng thái TIM_VIEC.")
                bot_state = "TIM_VIEC"
                action_taken = True
        elif bot_state == "TIM_VIEC":
            # Ưu tiên tìm nút đóng trước khi xem quảng cáo mới
            best_match_high = find_best_match_in_folder(screen_gray, high_prio_templates, HIGH_PRIORITY_FOLDER, use_roi=False)
            if best_match_high:
                click_at(best_match_high["location"], best_match_high["path"])
                time.sleep(2)
                action_taken = True
            if not action_taken:
                best_match_low = find_best_match_in_folder(screen_gray, low_prio_templates, LOW_PRIORITY_FOLDER)
                if best_match_low:
                    click_at(best_match_low["location"], best_match_low["path"])
                    logger.info(f"==> Đã nhấn xem QC. Bắt đầu chờ cố định {AD_WATCH_DELAY} giây...")
                    time.sleep(AD_WATCH_DELAY)
                    logger.info("==> Chờ cố định kết thúc. Chuyển sang trạng thái TIM_NUT_DONG.")
                    bot_state = "TIM_NUT_DONG"
                    action_taken = True
            if not action_taken:
                logger.info("Không tìm thấy việc gì để làm. Chờ...")
        
        # Adaptive sleep logic
        if action_taken:
            no_action_count = 0
            current_sleep_interval = SCAN_INTERVAL  # Reset to default
        else:
            no_action_count += 1
            if no_action_count >= reset_action_threshold:
                current_sleep_interval = min(current_sleep_interval * 1.5, max_sleep_interval)
                no_action_count = 0  # Reset count to avoid continuous increase
        
        logger.info(f"Sleeping for {current_sleep_interval:.2f} seconds...")
        time.sleep(current_sleep_interval)

if __name__ == "__main__":
    try: main()
    except KeyboardInterrupt: print("\n--- Bot đã dừng. ---")