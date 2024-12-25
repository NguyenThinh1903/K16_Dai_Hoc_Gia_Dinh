package org.example.useCase.addInvoice;


import org.example.entity.Invoice;

public interface AddInvoiceDatabaseBoundary {
    public void addInvoic(Invoice invoice);
}
