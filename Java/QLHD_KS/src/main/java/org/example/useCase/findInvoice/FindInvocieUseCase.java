package org.example.useCase.findInvoice;

import org.example.entity.Invoice;
import org.example.entity.InvoiceGio;
import org.example.entity.InvoiceNgay;

import java.util.ArrayList;
import java.util.List;

public class FindInvocieUseCase implements FindInvocieInputBoundary {
    private FindInvoiceDatabaseBoundary findInvoiceDatabaseBoundary;
    private FindInvoiceOutputBoundary findInvoiceOutputBoundary;

    public FindInvocieUseCase(FindInvoiceDatabaseBoundary findInvoiceDatabaseBoundary, FindInvoiceOutputBoundary findInvoiceOutputBoundary) {
        this.findInvoiceDatabaseBoundary = findInvoiceDatabaseBoundary;
        this.findInvoiceOutputBoundary = findInvoiceOutputBoundary;
    }

    @Override
    public void execute(FindInvoiceInputDTO requestData) {
        List<FindInvoiceOutputDTO> listInvoiceOutputDTO = new ArrayList<>();

        // Tạo đối tượng để phản hồi lỗi
        FindInvoiceOutputDTO responseError = new FindInvoiceOutputDTO();
        responseError.setStatus("error");

        // Kiểm tra tính hợp lệ của dữ liệu đầu vào
        if (!this.verify(requestData)) {
            responseError.setMessage("Dữ liệu không hợp lệ");
            findInvoiceOutputBoundary.exportError(responseError);
            return;
        }

        // Lấy dữ liệu từ request
        String maHD = requestData.getMaHD();
        String tenKH = requestData.getTenKH();
        String ngayHD = requestData.getNgayHD();

        // Tìm hóa đơn từ cơ sở dữ liệu dựa trên các tiêu chí đầu vào
        List<Invoice> listInvoice = findInvoiceDatabaseBoundary.findInvoice(maHD, tenKH, ngayHD);

        if (listInvoice == null) {
            responseError.setMessage("Đã xảy ra lỗi tại Database");
            findInvoiceOutputBoundary.exportError(responseError);
            return;
        }

        if (listInvoice.isEmpty()) {
            // Thông báo lỗi nếu không tìm thấy hóa đơn
            if (maHD != null && !maHD.isEmpty()) {
                responseError.setMessage("Không tìm thấy hoá đơn nào với mã hoá đơn : " + maHD);
            } else if (tenKH != null && !tenKH.isEmpty()) {
                responseError.setMessage("Không tìm thấy hoá đơn nào với tên khách hàng : " + tenKH);
            } else if (ngayHD != null && !ngayHD.isEmpty()) {
                responseError.setMessage("Không tìm thấy hoá đơn nào với ngày hoá đơn : " + ngayHD);
            } else {
                responseError.setMessage("Không tìm thấy hoá đơn nào phù hợp với tiêu chuẩn tìm kiếm");
            }

            findInvoiceOutputBoundary.exportError(responseError);
            return;
        }

        // Xử lý các hóa đơn tìm được và chuyển đổi thành đối tượng DTO
        for (Invoice invoice : listInvoice) {
            if (invoice instanceof InvoiceGio) {
                InvoiceGio invoiceGio = (InvoiceGio) invoice;
                listInvoiceOutputDTO.add(new FindInvoiceOutputDTO(
                        invoiceGio.getMaHD(),
                        invoiceGio.getTenKH(),
                        invoiceGio.getNgayHoaDon(),
                        invoiceGio.getMaPhong(),
                        invoiceGio.getDonGia(),
                        invoiceGio.getLoaiHoaDon(),
                        0,
                        invoiceGio.getSoGio(),
                        invoiceGio.tinhThanhTien()
                ));
            } else if (invoice instanceof InvoiceNgay) {
                InvoiceNgay invoiceNgay = (InvoiceNgay) invoice;
                listInvoiceOutputDTO.add(new FindInvoiceOutputDTO(
                        invoiceNgay.getMaHD(),
                        invoiceNgay.getTenKH(),
                        invoiceNgay.getNgayHoaDon(),
                        invoiceNgay.getMaPhong(),
                        invoiceNgay.getDonGia(),
                        invoiceNgay.getLoaiHoaDon(),
                        invoiceNgay.getSoNgay(),
                        0,
                        invoiceNgay.tinhThanhTien()
                ));
            }
        }

        // Trả kết quả tìm kiếm về cho output boundary
        findInvoiceOutputBoundary.exportResult(listInvoiceOutputDTO);
    }

    // Kiểm tra dữ liệu đầu vào có hợp lệ hay không (kiểm tra không trống)
    private boolean verify(FindInvoiceInputDTO findInvoiceInputDTO) {
        String maHD = findInvoiceInputDTO.getMaHD();
        String tenKH = findInvoiceInputDTO.getTenKH();

        // Kiểm tra nếu ít nhất một trong hai trường có giá trị hợp lệ (không phải null hoặc rỗng)
        if ((maHD != null && !maHD.trim().isEmpty()) ||
                (tenKH != null && !tenKH.trim().isEmpty())) {
            return true;
        } else {
            return false;
        }
    }
}
