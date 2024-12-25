package org.example.useCase.findInvoice;

import org.example.entity.Invoice;

import java.util.List;

public interface FindInvoiceDatabaseBoundary {
    public List<Invoice> findInvoice(String maHD, String tenKH, String ngayHD);
}
