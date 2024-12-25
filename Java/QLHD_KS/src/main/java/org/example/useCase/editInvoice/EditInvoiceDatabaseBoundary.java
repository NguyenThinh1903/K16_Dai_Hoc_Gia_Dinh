package org.example.useCase.editInvoice;

import org.example.entity.Invoice;

public interface EditInvoiceDatabaseBoundary {
    public void updateInvoice(Invoice invoice);
}
