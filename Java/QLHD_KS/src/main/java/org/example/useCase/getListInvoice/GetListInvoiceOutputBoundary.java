// GetListInvoiceOutputBoundary.java
package org.example.useCase.getListInvoice;

import java.util.List;

public interface GetListInvoiceOutputBoundary {
    public void exportError(GetListInvoiceOutputDTO responseError);
    public void exportResult(List<GetListInvoiceOutputDTO> responseResult);
}
