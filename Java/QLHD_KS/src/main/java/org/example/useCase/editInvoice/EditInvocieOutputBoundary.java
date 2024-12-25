package org.example.useCase.editInvoice;

public interface EditInvocieOutputBoundary {
    public void executeError(EditInvoiceOutputDTO responseError);
    public void executeResult(EditInvoiceOutputDTO responseResult);
}
