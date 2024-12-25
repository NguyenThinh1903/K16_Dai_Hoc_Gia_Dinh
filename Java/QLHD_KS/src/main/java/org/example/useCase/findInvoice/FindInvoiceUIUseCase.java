package org.example.useCase.findInvoice;

import org.example.ui.findInvoice.FindInvoiceView;

public class FindInvoiceUIUseCase  implements FindInvoiceUIInputBoundary{
   private FindInvoiceView findInvoiceView;

    public FindInvoiceUIUseCase(FindInvoiceView findInvoiceView) {
        this.findInvoiceView = findInvoiceView;
    }

    @Override
    public void execute() {
        this.findInvoiceView.mainShow();
    }
}
