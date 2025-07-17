import cv2
import numpy as np
from sklearn.svm import SVC
from tensorflow.keras.datasets import mnist

(X_train, y_train), (X_test, y_test) = mnist.load_data()
X_train = X_train.reshape(-1, 28*28) / 255.0
X_test = X_test.reshape(-1, 28*28) / 255.0

svm_model = SVC(kernel='rbf', random_state=42)
svm_model.fit(X_train, y_train)

def preprocess_image(image_path):
    img = cv2.imread(image_path, cv2.IMREAD_GRAYSCALE)
    img = cv2.resize(img, (28, 28), interpolation=cv2.INTER_AREA)
    _, img = cv2.threshold(img, 128, 255, cv2.THRESH_BINARY_INV)
    img = img / 255.0
    img = img.reshape(1, 28*28)
    return img

image_path = 'D:\\Downloads\\test_numberr.jpg' 
processed_img = preprocess_image(image_path)
prediction = svm_model.predict(processed_img)
print(f"Ký tự dự đoán: {prediction[0]}")

img_display = cv2.imread(image_path, cv2.IMREAD_GRAYSCALE)
import matplotlib.pyplot as plt
plt.imshow(img_display, cmap='gray')
plt.title(f"Dự đoán: {prediction[0]}")
plt.show()



# gg colab yolov8
# import pandas as pd
# import matplotlib.pyplot as plt
# import os

# run_dir = "/content/runs/detect/train" 
# csv_path = os.path.join(run_dir, "results.csv")


# if not os.path.exists(csv_path):
#     print(f"Lỗi: Không tìm thấy file 'results.csv' tại đường dẫn: {csv_path}")
# else:
#     print(f"Đang đọc dữ liệu từ: {csv_path}")
#     df = pd.read_csv(csv_path)

#     df.columns = df.columns.str.strip()
#     print("Các cột có trong file CSV:", df.columns.tolist())

#     try:
#         plt.figure(figsize=(12, 5))
#         plt.subplot(1, 2, 1)
#         plt.plot(df['epoch'], df['train/box_loss'], label='Train Box Loss')
#         plt.plot(df['epoch'], df['train/cls_loss'], label='Train Class Loss')
#         plt.plot(df['epoch'], df['val/box_loss'], label='Validation Box Loss')
#         plt.plot(df['epoch'], df['val/cls_loss'], label='Validation Class Loss')
#         plt.title('Biểu đồ Loss qua các Epoch')
#         plt.xlabel('Epoch')
#         plt.ylabel('Loss')
#         plt.legend()
#         plt.grid(True)

#         plt.subplot(1, 2, 2)
#         plt.plot(df['epoch'], df['metrics/precision(B)'], label='Precision')
#         plt.plot(df['epoch'], df['metrics/recall(B)'], label='Recall')
#         plt.plot(df['epoch'], df['metrics/mAP50(B)'], label='mAP@0.5', linewidth=2)
#         plt.plot(df['epoch'], df['metrics/mAP50-95(B)'], label='mAP@0.5:0.95', linewidth=2)
#         plt.title('Các chỉ số hiệu suất qua các Epoch')
#         plt.xlabel('Epoch')
#         plt.ylabel('Score')
#         plt.legend()
#         plt.grid(True)
        
#         plt.tight_layout()
#         plt.show()

#     except KeyError as e:
#         print(f"Lỗi: Không tìm thấy cột {e} trong file CSV.")
#         print("Vui lòng kiểm tra lại tên các cột đã in ở trên và chỉnh sửa code cho phù hợp.")