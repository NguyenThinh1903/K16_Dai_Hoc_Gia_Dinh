package org.example.ui.findInvoice;

import org.example.useCase.findInvoice.FindInvocieInputBoundary;
import org.example.useCase.findInvoice.FindInvoiceInputDTO;

public class FindInvoiceController {
    private FindInvocieInputBoundary findInvoiceInputBoundary;

    public FindInvoiceController(FindInvocieInputBoundary findInvoiceInputBoundary) {
        this.findInvoiceInputBoundary = findInvoiceInputBoundary;
    }

    public void execute(FindInvoiceInputDTO findInvoiceInputDTO) {
        this.findInvoiceInputBoundary.execute(findInvoiceInputDTO);
    }
}
