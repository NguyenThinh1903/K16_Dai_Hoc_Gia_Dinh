package org.example.useCase.deleteInvoice;

import org.example.ui.deleteInvoice.DeleteInvoicePresenter;
import org.example.ui.deleteInvoice.DeleteInvoiceView;

public class DeleteInvoiceUIUseCase implements DeleteInvoiceUIInputBoundary {
    public DeleteInvoiceUIUseCase(DeleteInvoiceUIPresenter deleteInvoiceUIPresenter) {
        this.deleteInvoiceUIPresenter = deleteInvoiceUIPresenter;
    }

    //    private DeleteInvoiceView deleteInvoiceView;
    private DeleteInvoiceUIPresenter deleteInvoiceUIPresenter;


    @Override
    public void execute(DeleteInvoiceUIInputDTO deleteInvoiceUIInputDTO) {
        DeleteInvoiceUiOutptDTO deleteInvoiceUiOutptDTO = new DeleteInvoiceUiOutptDTO();
        deleteInvoiceUiOutptDTO.setMaHD(deleteInvoiceUIInputDTO.getMaHD());
        deleteInvoiceUIPresenter.present(deleteInvoiceUiOutptDTO);
    }
}
