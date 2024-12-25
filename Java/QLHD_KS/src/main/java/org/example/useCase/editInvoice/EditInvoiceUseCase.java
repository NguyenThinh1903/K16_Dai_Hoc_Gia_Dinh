package org.example.useCase.editInvoice;

import org.example.entity.Invoice;
import org.example.entity.InvoiceGio;
import org.example.entity.InvoiceNgay;
import org.example.ui.observer.Publisher;

import java.text.SimpleDateFormat;
import java.util.Date;

public class EditInvoiceUseCase extends Publisher implements EditInvoiceInputBoundary {
    private EditInvocieOutputBoundary editInvocieOutputBoundary;
    private EditInvoiceDatabaseBoundary editInvoiceDatabaseBoundary;

    public EditInvoiceUseCase(EditInvocieOutputBoundary editInvocieOutputBoundary, EditInvoiceDatabaseBoundary editInvoiceDatabaseBoundary) {
        this.editInvocieOutputBoundary = editInvocieOutputBoundary;
        this.editInvoiceDatabaseBoundary = editInvoiceDatabaseBoundary;
    }

    @Override
    public void update(EditInvoiceInputDTO requestData) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        EditInvoiceOutputDTO responseError = new EditInvoiceOutputDTO();
        responseError.setStatus("error");
        Invoice invoice;

        // Debug: In ra toàn bộ dữ liệu nhận được
        System.out.println("DEBUG - Dữ liệu nhận được:");
        System.out.println("MaHD: " + requestData.getMaHD());
        System.out.println("TenKH: " + requestData.getTenKH());
        System.out.println("NgayHD: " + requestData.getNgayHD());
        System.out.println("MaPhong: " + requestData.getMaPhong());
        System.out.println("LoaiHoaDon: " + requestData.getLoaiHoaDon());
        System.out.println("DonGia: " + requestData.getDonGia());
        System.out.println("SoNgay: " + requestData.getSoNgay());
        System.out.println("SoGio: " + requestData.getSoGio());

        // Kiểm tra dữ liệu hợp lệ trước khi cập nhật
        String validationError = verifyUpdateData(requestData);
        if (!validationError.isEmpty()) {
            responseError.setMessage(validationError);
            editInvocieOutputBoundary.executeError(responseError);
            return;
        }

        // Kiểm tra ngày hóa đơn
        Date ngayHD;
        try {
            ngayHD = dateFormat.parse(requestData.getNgayHD());
        } catch (Exception e) {
            responseError.setMessage("Ngày hóa đơn không hợp lệ!");
            editInvocieOutputBoundary.executeError(responseError);
            return;
        }

        double donGia;
        try {
            donGia = Double.parseDouble(requestData.getDonGia());
            if (donGia <= 0) throw new NumberFormatException(); // Kiểm tra đơn giá > 0
        } catch (NumberFormatException e) {
            responseError.setMessage("Đơn giá không hợp lệ!");
            editInvocieOutputBoundary.executeError(responseError);
            return;
        }

        String loaiHoaDon = requestData.getLoaiHoaDon();
        int soNgay = 0;
        int soGio = 0;

        try {
            if (loaiHoaDon.equals("Theo ngày")) {
                soNgay = Integer.parseInt(requestData.getSoNgay());
                if (soNgay <= 0) throw new NumberFormatException(); // Kiểm tra số ngày > 0
            } else if (loaiHoaDon.equals("Theo giờ")) {
                soGio = Integer.parseInt(requestData.getSoGio());
                if (soGio <= 0) throw new NumberFormatException(); // Kiểm tra số giờ > 0
            } else {
                responseError.setMessage("Loại hóa đơn không hợp lệ!");
                editInvocieOutputBoundary.executeError(responseError);
                return;
            }
        } catch (NumberFormatException e) {
            responseError.setMessage("Số ngày hoặc số giờ không hợp lệ!");
            editInvocieOutputBoundary.executeError(responseError);
            return;
        }

        int maHD;
        try {
            maHD = Integer.parseInt(requestData.getMaHD());
        } catch (NumberFormatException e) {
            responseError.setMessage("Mã hóa đơn không hợp lệ!");
            editInvocieOutputBoundary.executeError(responseError);
            return;
        }

        String tenKH = requestData.getTenKH();
        String maPhong = requestData.getMaPhong();

        // Tạo hóa đơn dựa trên loại hóa đơn
        if (loaiHoaDon.equals("Theo ngày")) {
            invoice = new InvoiceNgay(maHD, tenKH, ngayHD, maPhong, donGia, loaiHoaDon, soNgay);
        } else {
            invoice = new InvoiceGio(maHD, tenKH, ngayHD, maPhong, donGia, loaiHoaDon, soGio);
        }

        // Cập nhật hóa đơn vào cơ sở dữ liệu
        editInvoiceDatabaseBoundary.updateInvoice(invoice);

        // Trả về kết quả thành công
        EditInvoiceOutputDTO responseData = new EditInvoiceOutputDTO();
        responseData.setStatus("success");
        responseData.setMessage("Đã sửa thành công hóa đơn: " + maHD);
        editInvocieOutputBoundary.executeResult(responseData);
        notifySubscribers();
    }

    private String verifyUpdateData(EditInvoiceInputDTO requestData) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            // Kiểm tra tên khách hàng
            String tenKH = requestData.getTenKH();
            if (tenKH == null || tenKH.isEmpty()) {
                return "Tên khách hàng không được bỏ trống!";
            }
            if (tenKH.matches(".*\\d.*")) {
                return "Tên khách hàng không được chứa số!";
            }

            // Kiểm tra ngày hóa đơn
            Date ngayHD;
            try {
                ngayHD = dateFormat.parse(requestData.getNgayHD());
            } catch (Exception e) {
                return "Ngày hóa đơn không hợp lệ!";
            }

            // Kiểm tra ngày hóa đơn không lớn hơn ngày hiện tại
            Date ngayHienTai = new Date();
            if (ngayHD.after(ngayHienTai)) {
                return "Ngày hóa đơn không được lớn hơn ngày hiện tại!";
            }

            // Kiểm tra năm của ngày hóa đơn
            int namHienTai = Integer.parseInt(new SimpleDateFormat("yyyy").format(ngayHienTai));
            int namHoaDon = Integer.parseInt(new SimpleDateFormat("yyyy").format(ngayHD));
            if (namHoaDon < namHienTai - 3) {
                return "Năm của ngày hóa đơn không được nhỏ hơn 3 năm trước!";
            }

            // Kiểm tra mã phòng
            String maPhong = requestData.getMaPhong();
            if (maPhong == null || maPhong.isEmpty()) {
                return "Mã phòng không được bỏ trống!";
            }

            // Kiểm tra loại hóa đơn
            String loaiHoaDon = requestData.getLoaiHoaDon();
            if (loaiHoaDon == null || loaiHoaDon.isEmpty()) {
                return "Loại hóa đơn không được bỏ trống!";
            }

            // Kiểm tra đơn giá
            try {
                double donGia = Double.parseDouble(requestData.getDonGia());
                if (donGia <= 0) {
                    return "Đơn giá phải lớn hơn 0!";
                }
            } catch (NumberFormatException e) {
                return "Đơn giá không hợp lệ!";
            }

            // Kiểm tra số ngày hoặc số giờ
            if (loaiHoaDon.equals("Theo ngày")) {
                try {
                    int soNgay = Integer.parseInt(requestData.getSoNgay());
                    if (soNgay <= 0) {
                        return "Số ngày phải lớn hơn 0!";
                    }
                } catch (NumberFormatException e) {
                    return "Số ngày không hợp lệ!";
                }
            } else if (loaiHoaDon.equals("Theo giờ")) {
                try {
                    int soGio = Integer.parseInt(requestData.getSoGio());
                    if (soGio <= 0) {
                        return "Số giờ phải lớn hơn 0!";
                    }
                } catch (NumberFormatException e) {
                    return "Số giờ không hợp lệ!";
                }
            } else {
                return "Loại hóa đơn không hợp lệ!";
            }

        } catch (Exception ex) {
            return "Một lỗi không xác định đã xảy ra!";
        }
        return "";
    }
}
