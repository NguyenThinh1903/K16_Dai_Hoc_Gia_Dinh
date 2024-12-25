package org.example.ui.quanLyHDKhachSan;

import org.example.entity.Invoice;
import org.example.useCase.getListInvoice.GetListInvoiceUIInputBoundary;
import org.example.useCase.quanLyHoaDon_KS.QuanLyHDKhachSanInputBoundary;
import org.example.useCase.quanLyHoaDon_KS.QuanLyHDKhachSanInputDTO;
import org.example.useCase.quanLyHoaDon_KS.QuanLyHDKhachSanOutputDTO;

import java.util.List;

public class QuanLyHDKhachSanController {
    private final QuanLyHDKhachSanInputBoundary quanLyHDKhachSanInputBoundary;
    private GetListInvoiceUIInputBoundary getListInvoiceUIInputBoundary;
    public QuanLyHDKhachSanController(QuanLyHDKhachSanInputBoundary quanLyHDKhachSanInputBoundary) {
        this.quanLyHDKhachSanInputBoundary = quanLyHDKhachSanInputBoundary;
    }

    public QuanLyHDKhachSanController(QuanLyHDKhachSanInputBoundary quanLyHDKhachSanInputBoundary, GetListInvoiceUIInputBoundary getListInvoiceUIInputBoundary) {
        this.quanLyHDKhachSanInputBoundary = quanLyHDKhachSanInputBoundary;
        this.getListInvoiceUIInputBoundary = getListInvoiceUIInputBoundary;
    }

    public List<Invoice> getListInvoice(){
        return getListInvoiceUIInputBoundary.execute();
    }

    public void execute(QuanLyHDKhachSanInputDTO quanLyHDKhachSanInputDTO){
        this.quanLyHDKhachSanInputBoundary.execute(quanLyHDKhachSanInputDTO);
        if (getListInvoiceUIInputBoundary != null){
            getListInvoiceUIInputBoundary.execute();
        }
    }
}
