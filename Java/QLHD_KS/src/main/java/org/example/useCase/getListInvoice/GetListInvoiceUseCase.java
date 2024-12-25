package org.example.useCase.getListInvoice;

import org.example.entity.Invoice;
import org.example.entity.InvoiceGio;
import org.example.entity.InvoiceNgay;

import java.util.ArrayList;
import java.util.List;

public class GetListInvoiceUseCase implements GetListInvoiceInputBoundary{
    private GetListInvoiceOutputBoundary getListInvoiceOutputBoundary;
    private GetListInvoiceDatabaseBoundary getListInvoiceDatabaseBoundary;

    public GetListInvoiceUseCase(GetListInvoiceOutputBoundary getListInvoiceOutputBoundary, GetListInvoiceDatabaseBoundary getListInvoiceDatabaseBoundary) {
        this.getListInvoiceOutputBoundary = getListInvoiceOutputBoundary;
        this.getListInvoiceDatabaseBoundary = getListInvoiceDatabaseBoundary;
    }

    @Override
    public void execute() {
        GetListInvoiceOutputDTO responseError = new GetListInvoiceOutputDTO();
        responseError.setStatus("error");
        List <GetListInvoiceOutputDTO> listOutputDTO = new ArrayList<>();

        List<Invoice> listInvoice = getListInvoiceDatabaseBoundary.getAllInvoice();

        if (listInvoice == null){
            responseError.setMessage("không có hoá đơn nào ");
            getListInvoiceOutputBoundary.exportError(responseError);
        } else {
            for (Invoice  invoice : listInvoice){
                if (invoice.getClass().equals(InvoiceGio.class)){
                    InvoiceGio invoiceGio = (InvoiceGio) invoice;
                    listOutputDTO.add(new GetListInvoiceOutputDTO(invoiceGio.getMaHD(), invoiceGio.getTenKH(), invoiceGio.getNgayHoaDon(), invoiceGio.getMaPhong(), invoiceGio.getDonGia(), invoiceGio.getLoaiHoaDon(), 0, invoiceGio.getSoGio(), invoiceGio.tinhThanhTien()));
                } else {
                    InvoiceNgay invoiceNgay = (InvoiceNgay) invoice;
                    listOutputDTO.add(new GetListInvoiceOutputDTO(invoiceNgay.getMaHD(), invoiceNgay.getTenKH(), invoiceNgay.getNgayHoaDon(), invoiceNgay.getMaPhong(), invoiceNgay.getDonGia(), invoiceNgay.getLoaiHoaDon(), invoiceNgay.getSoNgay(), 0, invoiceNgay.tinhThanhTien()));
                }
            }
            getListInvoiceOutputBoundary.exportResult(listOutputDTO);
        }

    }
}
