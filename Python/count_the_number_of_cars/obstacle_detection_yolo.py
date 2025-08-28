# git clone https://github.com/nianticlabs/monodepth2.git
# hoặc tải ZIP giải nén thư mục monodepth2 đặt cùng folder với file này
# pip install -r requirements.txt
# python obstacle_detection_yolo.py --source input.mp4 --show --save

import argparse
import cv2
import torch
import numpy as np
from ultralytics import YOLO
import warnings
import sys
import os

# Tắt warnings không quan trọng
warnings.filterwarnings("ignore")

# Đảm bảo Python tìm được layers.py trong monodepth2
sys.path.append(os.path.join(os.path.dirname(__file__), "monodepth2"))

# Import từ monodepth2
from monodepth2.layers import disp_to_depth
import monodepth2.networks as networks


class DepthEstimator:
    def __init__(self, device="cuda"):
        self.device = device

        encoder_path = "monodepth2/models/mono+stereo_640x192/encoder.pth"
        depth_path = "monodepth2/models/mono+stereo_640x192/depth.pth"

        # Load encoder
        self.encoder = networks.ResnetEncoder(18, False)
        loaded_dict_enc = torch.load(encoder_path, map_location=device)
        self.encoder.load_state_dict(
            {k: v for k, v in loaded_dict_enc.items() if k in self.encoder.state_dict()}
        )
        self.encoder.to(device)
        self.encoder.eval()

        # Load decoder
        self.depth_decoder = networks.DepthDecoder(num_ch_enc=self.encoder.num_ch_enc, scales=range(4))
        loaded_dict = torch.load(depth_path, map_location=device)
        self.depth_decoder.load_state_dict(loaded_dict)
        self.depth_decoder.to(device)
        self.depth_decoder.eval()

    def predict_depth(self, img_bgr):
        img_resized = cv2.resize(img_bgr, (640, 192))
        img_input = torch.from_numpy(img_resized).permute(2, 0, 1).float().unsqueeze(0) / 255.0
        img_input = img_input.to(self.device)

        with torch.no_grad():
            features = self.encoder(img_input)
            outputs = self.depth_decoder(features)
            disp = outputs[("disp", 0)]
            _, depth = disp_to_depth(disp, 0.1, 100)

        depth_resized = cv2.resize(depth[0, 0].cpu().numpy(), (img_bgr.shape[1], img_bgr.shape[0]))
        return depth_resized


def main(args):
    device = "cuda" if torch.cuda.is_available() else "cpu"
    print(f"Using device: {device}")

    # Load YOLOv8
    yolo = YOLO(args.yolo_model)

    # Load Monodepth2
    depth_estimator = DepthEstimator(device)

    # Mở video hoặc camera
    cap = cv2.VideoCapture(args.source)
    print("cap.isOpened():", cap.isOpened())
    if not cap.isOpened():
        print(f"❌ Không mở được video: {args.source}")
        return

    # Chuẩn bị ghi video
    writer = None

    while True:
        ret, frame = cap.read()
        if not ret:
            print("⏹ Video kết thúc hoặc không đọc được frame.")
            break

        # YOLO detection
        results = yolo(frame, imgsz=args.imgsz, conf=args.conf, iou=args.iou, verbose=False)[0]

        # Depth estimation
        depth_map = depth_estimator.predict_depth(frame)

        for box in results.boxes:
            cls = int(box.cls[0])
            label = yolo.names[cls]
            conf = float(box.conf[0])

            x1, y1, x2, y2 = map(int, box.xyxy[0])
            roi_depth = depth_map[y1:y2, x1:x2]
            if roi_depth.size > 0:
                median_depth = np.median(roi_depth)
                distance_m = median_depth * args.scale_m_per_unit
            else:
                distance_m = -1

            # Vẽ bounding box
            cv2.rectangle(frame, (x1, y1), (x2, y2), (0, 255, 0), 2)
            cv2.putText(
                frame,
                f"{label} {conf:.2f}, {distance_m:.2f}m",
                (x1, max(0, y1 - 5)),
                cv2.FONT_HERSHEY_SIMPLEX,
                0.6,
                (0, 255, 0),
                2,
            )

        # Hiển thị video
        if args.show:
            cv2.imshow("YOLOv8 + Monodepth2", frame)
            key = cv2.waitKey(30) & 0xFF
            if key == ord("q"):
                print("⏹ Người dùng dừng video.")
                break

        # Ghi video
        if args.save:
            if writer is None:
                fourcc = cv2.VideoWriter_fourcc(*"XVID")
                writer = cv2.VideoWriter("output.avi", fourcc, 20.0, (frame.shape[1], frame.shape[0]))
            writer.write(frame)

    cap.release()
    if writer is not None:
        writer.release()
        print("✅ Video lưu tại: output.avi")
    cv2.destroyAllWindows()


if __name__ == "__main__":
    parser = argparse.ArgumentParser()
    parser.add_argument("--source", type=str, default="obstacle.mp4", help="Camera index hoặc video path")
    parser.add_argument("--yolo-model", type=str, default="yolov8n.pt", help="YOLOv8 model")
    parser.add_argument("--imgsz", type=int, default=640, help="YOLO image size")
    parser.add_argument("--conf", type=float, default=0.5, help="YOLO confidence threshold")
    parser.add_argument("--iou", type=float, default=0.45, help="YOLO IOU threshold")
    parser.add_argument("--show", action="store_true", help="Hiển thị video")
    parser.add_argument("--save", action="store_true", help="Lưu video kết quả")
    parser.add_argument("--scale_m_per_unit", type=float, default=10.0, help="Hệ số chuyển depth unit sang mét")
    args = parser.parse_args()

    main(args)
