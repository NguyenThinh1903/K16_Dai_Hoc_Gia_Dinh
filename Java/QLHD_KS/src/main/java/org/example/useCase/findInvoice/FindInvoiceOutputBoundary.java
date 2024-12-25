package org.example.useCase.findInvoice;

import java.util.List;

public interface FindInvoiceOutputBoundary {
    public void exportError(FindInvoiceOutputDTO responseError);
    public void exportResult(List<FindInvoiceOutputDTO> listInvoice);
}
