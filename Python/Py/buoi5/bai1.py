def tinh_roi():
  """
  Chương trình tính ROI và đưa ra lời khuyên đầu tư.
  """
  while True:
    try:
      doanh_thu = float(input("Nhập doanh thu (VNĐ): "))
      if doanh_thu < 0:
        print("Doanh thu phải là số dương. Vui lòng nhập lại.")
        continue

      chi_phi = float(input("Nhập chi phí (VNĐ): "))
      if chi_phi <= 0: 
        print("Chi phí phải là số dương và khác 0. Vui lòng nhập lại.")
        continue

      break 
    except ValueError:
      print("Lỗi: Doanh thu và chi phí phải là số. Vui lòng nhập lại.")

  roi = (doanh_thu - chi_phi) / chi_phi
  print(f"Tỷ lệ ROI của dự án là: {roi:.2f}") # roi:.2f để in ra 2 chữ số sau dấu phẩy

  if roi >= 0.75:
    print("ROI >= 0.75.  ==>  NÊN đầu tư vào dự án này.")
  else:
    print("ROI < 0.75.  ==>  KHÔNG NÊN đầu tư vào dự án này.")

tinh_roi()