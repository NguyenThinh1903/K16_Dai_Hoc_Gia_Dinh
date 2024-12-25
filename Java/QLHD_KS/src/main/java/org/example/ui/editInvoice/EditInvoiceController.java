package org.example.ui.editInvoice;

import org.example.useCase.editInvoice.EditInvoiceInputBoundary;
import org.example.useCase.editInvoice.EditInvoiceInputDTO;

public class EditInvoiceController {
    private EditInvoiceInputBoundary editInvoiceInputBoundary;

    public EditInvoiceController(EditInvoiceInputBoundary editInvoiceInputBoundary) {
        this.editInvoiceInputBoundary = editInvoiceInputBoundary;
    }
    public void execute(EditInvoiceInputDTO editInvoiceInputDTO){
        this.editInvoiceInputBoundary.update(editInvoiceInputDTO);
    }
}
