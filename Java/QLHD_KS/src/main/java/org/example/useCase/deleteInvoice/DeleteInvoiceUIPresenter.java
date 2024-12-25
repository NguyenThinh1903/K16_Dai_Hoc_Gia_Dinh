package org.example.useCase.deleteInvoice;

import org.example.ui.deleteInvoice.DeleteInvoiceView;

public class DeleteInvoiceUIPresenter {
    private DeleteInvoiceUIView deleteInvoiceUIView;
    private DeleteInvoiceUIViewModel deleteInvoiceUIViewModel;

    public void present(DeleteInvoiceUiOutptDTO deleteInvoiceUiOutptDTO){
        String maHD = deleteInvoiceUiOutptDTO.getMaHD();
        deleteInvoiceUIViewModel.maHD = maHD;
        this.deleteInvoiceUIView.mainShow(deleteInvoiceUIViewModel);
    }

    public DeleteInvoiceUIPresenter(DeleteInvoiceUIView deleteInvoiceUIView, DeleteInvoiceUIViewModel deleteInvoiceUIViewModel) {
        this.deleteInvoiceUIView = deleteInvoiceUIView;
        this.deleteInvoiceUIViewModel = deleteInvoiceUIViewModel;
    }
}
