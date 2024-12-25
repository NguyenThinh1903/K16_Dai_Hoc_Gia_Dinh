package org.example.ui.addInvoice;

import org.example.useCase.addInvoice.AddInvoiceInputBoundary;
import org.example.useCase.addInvoice.AddInvoiceInputDTO;

public class AddInvoiceController {
    private AddInvoiceInputBoundary addInvoiceInputBoundary;

    public AddInvoiceController(AddInvoiceInputBoundary addInvoiceInputBoundary) {
        this.addInvoiceInputBoundary = addInvoiceInputBoundary;
    }

    public void execute(AddInvoiceInputDTO addInvoiceInputDTO) {
        this.addInvoiceInputBoundary.execute(addInvoiceInputDTO);
    }
}
