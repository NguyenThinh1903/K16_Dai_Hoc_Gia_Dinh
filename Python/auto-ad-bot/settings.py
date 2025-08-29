# --- CẤU HÌNG ---
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