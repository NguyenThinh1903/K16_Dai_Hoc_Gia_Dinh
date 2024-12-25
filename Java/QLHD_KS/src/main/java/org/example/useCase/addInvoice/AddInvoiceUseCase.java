package org.example.useCase.addInvoice;

import org.example.entity.Invoice;
import org.example.entity.InvoiceGio;
import org.example.entity.InvoiceNgay;
import org.example.ui.observer.Publisher;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Year;
import java.util.Date;

public class AddInvoiceUseCase extends Publisher implements AddInvoiceInputBoundary {
    private AddInvoiceOutputBoundary addInvoiceOutputBoundary;
    private AddInvoiceDatabaseBoundary addInvoiceDatabaseBoundary;

    public AddInvoiceUseCase(AddInvoiceDatabaseBoundary addInvoiceDatabaseBoundary, AddInvoiceOutputBoundary addInvoiceOutputBoundary) {
        this.addInvoiceDatabaseBoundary = addInvoiceDatabaseBoundary;
        this.addInvoiceOutputBoundary = addInvoiceOutputBoundary;
    }

    @Override
    public void execute(AddInvoiceInputDTO requestData) {
        SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
        AddInvoiceOutputDTO responseError = new AddInvoiceOutputDTO();
        responseError.setStatus("error");
        Invoice invoice;

        // Kiểm tra dữ liệu đầu vào và trả về lỗi cụ thể
        String verificationError = inputVerify(requestData);
        if (verificationError != null) {
            responseError.setMessage(verificationError);
            addInvoiceOutputBoundary.executeError(responseError);
            return;
        }

        String tenKH = requestData.getTenKH();
        Date ngayHD;
        String loaiHoaDon = requestData.getLoaiHoaDon();
        double donGia = Double.parseDouble(requestData.getDonGia());
        String maPhong = requestData.getMaPhong();

        try {
            ngayHD = formatDate.parse(requestData.getNgayHoaDon());
        } catch (ParseException e) {
            responseError.setMessage("Ngày hoá đơn sai định dạng");
            addInvoiceOutputBoundary.executeError(responseError);
            return;
        }

        // Tạo hoá đơn và lưu hoá đơn vào database
        if (loaiHoaDon.equals("Theo giờ")) {
            try {
                int soGio = Integer.parseInt(requestData.getSoGio());
                if (soGio <= 0) {
                    responseError.setMessage("Số giờ phải lớn hơn 0");
                    addInvoiceOutputBoundary.executeError(responseError);
                    return;
                }
                InvoiceGio invoiceGio = new InvoiceGio();
                invoiceGio.setTenKH(tenKH);
                invoiceGio.setNgayHoaDon(ngayHD);
                invoiceGio.setMaPhong(maPhong);
                invoiceGio.setDonGia(donGia);
                invoiceGio.setSoGio(soGio);
                invoiceGio.setLoaiHoaDon(loaiHoaDon);
                invoice = invoiceGio;
            } catch (NumberFormatException e) {
                responseError.setMessage("Số giờ không hợp lệ");
                addInvoiceOutputBoundary.executeError(responseError);
                return;
            }
        } else if (loaiHoaDon.equals("Theo ngày")) {
            try {
                int soNgay = Integer.parseInt(requestData.getSoNgay());
                if (soNgay <= 0) {
                    responseError.setMessage("Số ngày phải lớn hơn 0");
                    addInvoiceOutputBoundary.executeError(responseError);
                    return;
                }
                InvoiceNgay invoiceNgay = new InvoiceNgay();
                invoiceNgay.setTenKH(tenKH);
                invoiceNgay.setNgayHoaDon(ngayHD);
                invoiceNgay.setMaPhong(maPhong);
                invoiceNgay.setDonGia(donGia);
                invoiceNgay.setLoaiHoaDon(loaiHoaDon);
                invoiceNgay.setSoNgay(soNgay);
                invoice = invoiceNgay;
            } catch (NumberFormatException e) {
                responseError.setMessage("Số ngày không hợp lệ");
                addInvoiceOutputBoundary.executeError(responseError);
                return;
            }
        } else {
            responseError.setMessage("Vui lòng chọn loại hoá đơn hợp lệ");
            addInvoiceOutputBoundary.executeError(responseError);
            return;
        }

        // Lưu hoá đơn vào database
        this.addInvoiceDatabaseBoundary.addInvoic(invoice);

        // Trả về kết quả thành công
        AddInvoiceOutputDTO responseData = new AddInvoiceOutputDTO();
        responseData.setStatus("Success");
        responseData.setMessage("Đã thêm thành công!");
//        tenKH + tenKH
        addInvoiceOutputBoundary.executeResult(responseData);
        notifySubscribers();
    }

    // Hàm inputVerify kiểm tra dữ liệu đầu vào và trả về lỗi cụ thể
    private String inputVerify(AddInvoiceInputDTO requestData) {
        if (requestData.getTenKH() == null || requestData.getTenKH().trim().isEmpty()) {
            return "Tên khách hàng không được để trống";
        }
        if (requestData.getTenKH().matches(".*\\d.*")) {
            return "Tên khách hàng không được chứa số";
        }
        if (requestData.getNgayHoaDon() == null || requestData.getNgayHoaDon().trim().isEmpty()) {
            return "Ngày hóa đơn không được để trống";
        }
        if (requestData.getMaPhong() == null || requestData.getMaPhong().trim().isEmpty()) {
            return "Mã phòng không được để trống";
        }
        if (requestData.getDonGia() == null || requestData.getDonGia().trim().isEmpty()) {
            return "Đơn giá không được để trống";
        }
        if (requestData.getLoaiHoaDon() == null || requestData.getLoaiHoaDon().trim().isEmpty()) {
            return "Loại hóa đơn không được để trống";
        }

        // Kiểm tra số giờ nếu loại hóa đơn là "Theo giờ"
        if (requestData.getLoaiHoaDon().equalsIgnoreCase("theo giờ") &&
                (requestData.getSoGio() == null || requestData.getSoGio().trim().isEmpty())) {
            return "Số giờ không được để trống";
        }

        // Kiểm tra số ngày nếu loại hóa đơn là "Theo ngày"
        if (requestData.getLoaiHoaDon().equalsIgnoreCase("theo ngày") &&
                (requestData.getSoNgay() == null || requestData.getSoNgay().trim().isEmpty())) {
            return "Số ngày không được để trống";
        }

        try {
            double donGia = Double.parseDouble(requestData.getDonGia());
            if (donGia <= 0) {
                return "Đơn giá phải lớn hơn 0";
            }
            SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
            Date ngayHD = formatDate.parse(requestData.getNgayHoaDon());
            Date currentDate = new Date();
            if (ngayHD.after(currentDate)) {
                return "Ngày hóa đơn không được lớn hơn ngày hiện tại";
            }
            if (ngayHD.getYear() < currentDate.getYear() -3){
                return "không thể thêm hóa đơn sau 3 năm";
            }
        } catch (NumberFormatException e) {
            return "Đơn giá không hợp lệ";
        } catch (ParseException e) {
            return "Ngày hóa đơn sai định dạng";
        }
        return null;
    }

}
