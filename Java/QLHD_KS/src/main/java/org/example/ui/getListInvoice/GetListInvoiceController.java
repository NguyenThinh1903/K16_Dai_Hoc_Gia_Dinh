package org.example.ui.getListInvoice;//package nhom4.ui.getListInvoice;


import org.example.useCase.getListInvoice.GetListInvoiceInputBoundary;

public class GetListInvoiceController {
    private GetListInvoiceInputBoundary getListInvoiceInputBoundary;

    public GetListInvoiceController(GetListInvoiceInputBoundary getListInvoiceInputBoundary) {
        this.getListInvoiceInputBoundary = getListInvoiceInputBoundary;
    }

    public void execute() {
        this.getListInvoiceInputBoundary.execute();
    }
}
