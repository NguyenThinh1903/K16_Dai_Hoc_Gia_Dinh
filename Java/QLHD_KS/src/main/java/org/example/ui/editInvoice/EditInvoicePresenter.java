package org.example.ui.editInvoice;

import org.example.useCase.editInvoice.EditInvocieOutputBoundary;
import org.example.useCase.editInvoice.EditInvoiceOutputDTO;
import org.example.useCase.editInvoice.EditInvoiceUIInputDTO;

public class EditInvoicePresenter implements EditInvocieOutputBoundary {
    private EditInvoiceView editInvoiceView;

    public EditInvoicePresenter(EditInvoiceView editInvoiceView, EditInvoiceViewModel editInvoiceViewModel) {
        this.editInvoiceView = editInvoiceView;
        this.editInvoiceViewModel = editInvoiceViewModel;
    }

    private EditInvoiceViewModel editInvoiceViewModel;
    @Override
    public void executeError(EditInvoiceOutputDTO responseError) {
        this.editInvoiceViewModel.status = responseError.getStatus();
        this.editInvoiceViewModel.message = responseError.getMessage();
        if (this.editInvoiceView != null){
            this.editInvoiceView.showMsgError(this.editInvoiceViewModel);
        }
    }

    @Override
    public void executeResult(EditInvoiceOutputDTO responseResult) {
        this.editInvoiceViewModel.status = responseResult.getStatus();
        this.editInvoiceViewModel.message = responseResult.getMessage();
        if (this.editInvoiceView != null){
            this.editInvoiceView.showMsgResult(this.editInvoiceViewModel);
        }
    }
}
