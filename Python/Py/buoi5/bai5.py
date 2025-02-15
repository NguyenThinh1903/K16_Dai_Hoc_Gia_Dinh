def xu_ly_chuoi(chuoi):
    """
    Xử lý chuỗi số phân tách bởi dấu chấm phẩy.

    Args:
        chuoi: Chuỗi đầu vào (ví dụ: "5;7;8;-2;8;11;13;9;10").

    Returns:
        None.  (Chỉ in kết quả, không trả về giá trị).
    """

    # Tách chuỗi thành danh sách các số
    cac_so = chuoi.split(";")
    cac_so_int = []
    for so_str in cac_so:
        try:
            so_int = int(so_str)
            cac_so_int.append(so_int)
        except ValueError:
            print(f"Lỗi: '{so_str}' không phải là số hợp lệ. Bỏ qua.")

    # 1. Xuất các chữ số trên cùng một dòng, cách nhau bởi dấu cách
    print("Các số trong chuỗi:", end=" ")  # In ra, nhưng không xuống dòng
    for so in cac_so_int:
        print(so, end=" ")  # In số, sau đó in một dấu cách, không xuống dòng
    print()  # In một dòng mới sau khi đã in hết các số

    # 2. Đếm số chẵn
    so_chan = 0
    for so in cac_so_int:
        if so % 2 == 0:
            so_chan += 1
    print(f"Số lượng số chẵn: {so_chan}")

    # 3. Đếm số âm
    so_am = 0
    for so in cac_so_int:
        if so < 0:
            so_am += 1
    print(f"Số lượng số âm: {so_am}")

    # 4. Đếm số nguyên tố (sử dụng lại hàm từ các câu trả lời trước)
    def la_so_nguyen_to(num):
        if num <= 1:
            return False
        for i in range(2, int(num**0.5) + 1):
            if num % i == 0:
                return False
        return True

    so_nguyen_to = 0
    for so in cac_so_int:
        if la_so_nguyen_to(so):
            so_nguyen_to += 1
    print(f"Số lượng số nguyên tố: {so_nguyen_to}")

    # 5. Tính giá trị trung bình
    if len(cac_so_int) > 0:  # Tránh chia cho 0 nếu danh sách rỗng
      trung_binh = sum(cac_so_int) / len(cac_so_int)
      print(f"Giá trị trung bình: {trung_binh:.2f}") # làm tròn 2 số thập phân
    else:
      print("Không có số hợp lệ để tính trung bình.")



# --- Chương trình chính ---
chuoi_dau_vao = "5;7;8;-2;8;11;13;9;10"
xu_ly_chuoi(chuoi_dau_vao)

chuoi_dau_vao2 = "5;7;8;-2;8;abc;11;13;9;10" #Thử trường hợp có phần tử không phải số
xu_ly_chuoi(chuoi_dau_vao2)