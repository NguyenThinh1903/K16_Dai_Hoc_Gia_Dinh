package org.example.useCase.addInvoice;
public interface AddInvoiceOutputBoundary {
public void executeError(AddInvoiceOutputDTO responseError);
public void executeResult(AddInvoiceOutputDTO responseData);
}
