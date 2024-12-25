package org.example.useCase.addInvoice;

import org.example.ui.addInvoice.AddInvoicePresenter;
import org.example.ui.addInvoice.AddInvoiceView;

public class AddInvoiceUIUseCase implements AddInvoiceUIInputBoundary{
    private AddInvoiceView addInvoiceView;
    private AddInvoicePresenter addInvoicePresenter;

    public AddInvoiceUIUseCase(AddInvoiceView addInvoiceView) {
        this.addInvoiceView = addInvoiceView;
    }


    @Override
    public void execute() {
        addInvoiceView.mainShow();
    }
}
