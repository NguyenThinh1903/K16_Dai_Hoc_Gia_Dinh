# import the necessary packages
import cv2
import imutils
import numpy as np
from distancefinder import DistanceFinder 


ref_width_inches = 4.0
ref_distance_inches = 24.0
images_directory = r"D:\Documents\test\images"   


df = DistanceFinder(ref_width_inches, ref_distance_inches)

# Tạo ảnh giả lập trắng 800x800
refImage = 255 * np.ones((800, 800, 3), dtype="uint8")

# Marker giả định (x, y, w, h)
refMarker = (100, 100, 50, 50)

# Calibrate bằng width giả định
df.calibrate(width=refMarker[2])

# Vẽ thử trên ảnh giả lập
distance = df.distance(refMarker[2])
refImage = df.draw(refImage, refMarker, distance)
cv2.imshow("Reference", refImage)

try:
    from imutils import paths
    for imagePath in paths.list_images(images_directory):
        filename = imagePath[imagePath.rfind("/") + 1:]
        image = cv2.imread(imagePath)
        if image is None:
            print(f"[INFO] không đọc được {filename}, bỏ qua.")
            continue

        image = imutils.resize(image, height=700)
        print("[INFO] processing {}".format(filename))

        # tìm marker
        marker = DistanceFinder.findSquareMarker(image)
        if marker is None:
            print("[INFO] could not find marker for {}".format(filename))
            continue

        # tính khoảng cách
        distance = df.distance(marker[2])

        # vẽ kết quả
        image = df.draw(image, marker, distance)
        cv2.imshow("Image", image)
        cv2.waitKey(0)
except Exception as e:
    print("[WARN] Không load được thư mục ảnh test:", e)

cv2.destroyAllWindows()
