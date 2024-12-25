package org.example.useCase.deleteInvoice;

public interface DeleteInvoiceDatabaseBoundary {
    public void deleteInvoice(int maHD);

    public Boolean isExist(int maHD);
}
