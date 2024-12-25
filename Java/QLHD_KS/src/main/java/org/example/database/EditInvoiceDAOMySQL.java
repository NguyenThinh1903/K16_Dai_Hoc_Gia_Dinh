package org.example.database;

import org.example.entity.Invoice;
import org.example.entity.InvoiceGio;
import org.example.entity.InvoiceNgay;
import org.example.useCase.editInvoice.EditInvoiceDatabaseBoundary;

import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;

public class EditInvoiceDAOMySQL extends DAOMySQL implements EditInvoiceDatabaseBoundary {
    public EditInvoiceDAOMySQL(String ipAddress, int port, String database, String username, String password) throws Exception {
        super(ipAddress, port, database, username, password);
    }

    @Override
    public void updateInvoice(Invoice invoice) {
        connect();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String sql = "UPDATE invoice SET tenKH = ?, ngayHD = ?, maPhong = ?, donGia = ?, loaiHD = ?, soNgay = ?, soGio = ?  WHERE maHD = ?;";

        try {
            PreparedStatement preparedStatement = getPrepareStatement(sql);
            preparedStatement.setString(1, invoice.getTenKH());
            preparedStatement.setString(2,dateFormat.format(invoice.getNgayHoaDon()));
            preparedStatement.setString(3, invoice.getMaPhong());
            preparedStatement.setDouble(4, invoice.getDonGia());
            if (invoice.getClass().equals(InvoiceGio.class)){
                InvoiceGio invoiceGio = (InvoiceGio) invoice;
                preparedStatement.setString(5, invoiceGio.getLoaiHoaDon());
                preparedStatement.setInt(6, 0);
                preparedStatement.setInt(7, invoiceGio.getSoGio());
            } else if (invoice.getClass().equals(InvoiceNgay.class)) {
                InvoiceNgay invoiceNgay =  (InvoiceNgay) invoice;
                preparedStatement.setString(5, invoiceNgay.getLoaiHoaDon());
                preparedStatement.setInt(6,invoiceNgay.getSoNgay());
                preparedStatement.setInt(7, 0);
            }
            preparedStatement.setInt(8, invoice.getMaHD());

            int rowAffected = preparedStatement.executeUpdate();
            if (rowAffected > 0){
                System.out.println("Cập nhật thành công hóa đơn: " + invoice.getMaHD());
            }else {
                System.out.println("Không tìm thấy hóa đơn với mã: " + invoice.getMaHD());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        close();
    }
}
