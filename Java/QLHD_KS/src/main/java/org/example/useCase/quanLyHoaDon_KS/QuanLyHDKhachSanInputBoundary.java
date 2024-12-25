package org.example.useCase.quanLyHoaDon_KS;

import org.example.useCase.addInvoice.AddInvoiceUIInputBoundary;
import org.example.useCase.deleteInvoice.DeleteInvoiceUIInputBoundary;
import org.example.useCase.editInvoice.EditInvoiceUIInputBoundary;
import org.example.useCase.findInvoice.FindInvoiceUIInputBoundary;
import org.example.useCase.getListInvoice.GetListInvoiceUIInputBoundary;

public interface QuanLyHDKhachSanInputBoundary {
    void setAddInvoiceUIInputBoundary(AddInvoiceUIInputBoundary addInvoiceUIInputBoundary);
    void setEditInvoiceUIInputBoundary(EditInvoiceUIInputBoundary editInvoiceUIInputBoundary);
    void setDeleteInvoiceUIInputBoundary(DeleteInvoiceUIInputBoundary deleteInvoiceUIInputBoundary);
    void setGetListInvoiceUIInputBoundary(GetListInvoiceUIInputBoundary getListInvoiceUIInputBoundary);
    void SetFindInvoiceUIInputBoundary(FindInvoiceUIInputBoundary findInvoiceUIInputBoundary);
    void execute(QuanLyHDKhachSanInputDTO quanLyHDKhachSanInputDTO);
}
