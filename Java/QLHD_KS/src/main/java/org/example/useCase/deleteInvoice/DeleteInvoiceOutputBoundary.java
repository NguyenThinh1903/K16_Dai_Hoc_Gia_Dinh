package org.example.useCase.deleteInvoice;

public interface DeleteInvoiceOutputBoundary {
    public void exportError(DeleteInvoiceOutputDTO responseError);

    public void present(DeleteInvoiceOutputDTO outputDTO);
}
