package org.example.ui.deleteInvoice;

import org.example.useCase.deleteInvoice.DeleteInvoiceInputBoundary;
import org.example.useCase.deleteInvoice.DeleteInvoiceInputDTO;

public class DeleteInvoiceController {
    private DeleteInvoiceInputBoundary deleteInvoiceInputBoundary;

    public DeleteInvoiceController(DeleteInvoiceInputBoundary deleteInvoiceInputBoundary) {
        this.deleteInvoiceInputBoundary = deleteInvoiceInputBoundary;
    }

    public void execute(DeleteInvoiceInputDTO deleteInvoiceInputDTO) {
        this.deleteInvoiceInputBoundary.execute(deleteInvoiceInputDTO);
    }
}
