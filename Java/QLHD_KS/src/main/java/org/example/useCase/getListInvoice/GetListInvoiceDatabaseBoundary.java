// GetListInvoiceDatabaseBoundary.java
package org.example.useCase.getListInvoice;

import org.example.entity.Invoice;

import java.util.List;

public interface GetListInvoiceDatabaseBoundary {
    public List<Invoice> getAllInvoice();
}
