package org.example.useCase.getListInvoice;

import org.example.entity.Invoice;
import org.example.ui.getListInvoice.GetListInvoiceView;

import java.util.List;

public class GetListInvoiceUIUseCase implements GetListInvoiceUIInputBoundary{
    GetListInvoiceView getListInvoiceView;


    public GetListInvoiceUIUseCase(GetListInvoiceView getListInvoiceView) {
        this.getListInvoiceView = getListInvoiceView;
    }

    @Override
    public List<Invoice> execute() {
        this.getListInvoiceView.mainShow();
        return null;
    }
}
