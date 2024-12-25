package org.example.database;

import org.example.entity.Invoice;
import org.example.entity.InvoiceGio;
import org.example.entity.InvoiceNgay;
import org.example.useCase.findInvoice.FindInvoiceDatabaseBoundary;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class FindInvoiceDAOMySQL extends DAOMySQL implements FindInvoiceDatabaseBoundary {
    public FindInvoiceDAOMySQL(String ipAddress, int port, String database, String username, String password) throws Exception {
        super(ipAddress, port, database, username, password);
    }


    @Override
    public List<Invoice> findInvoice(String maHD, String tenKH, String ngayHD) {
        List<Invoice> listInvoice = new ArrayList<>();
        connect();

        StringBuilder sqlBuilder = new StringBuilder("SELECT * FROM invoice WHERE 1=1 ");
        if (maHD != null) {
            sqlBuilder.append("AND maHD = ").append(maHD).append(" ");
        }
        if (tenKH != null && !tenKH.isEmpty()) {
            sqlBuilder.append("AND tenKH LIKE '%").append(tenKH).append("%' ");
        }
        if (ngayHD != null && !ngayHD.isEmpty()) {
            sqlBuilder.append("AND ngayHD = '").append(ngayHD).append("' ");
        }

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sqlBuilder.toString())) {

            while (resultSet.next()) {
                Invoice invoice = null;
                String loaiHD = resultSet.getString("loaiHD");

                if ("Theo ngày".equals(loaiHD)) {
                    invoice = new InvoiceNgay(
                            resultSet.getInt("maHD"),
                            resultSet.getString("tenKH"),
                            resultSet.getDate("ngayHD"),
                            resultSet.getString("maPhong"),
                            resultSet.getDouble("donGia"),
                            loaiHD,
                            resultSet.getInt("soNgay")
                    );
                } else if ("Theo giờ".equals(loaiHD)) {
                    invoice = new InvoiceGio(
                            resultSet.getInt("maHD"),
                            resultSet.getString("tenKH"),
                            resultSet.getDate("ngayHD"),
                            resultSet.getString("maPhong"),
                            resultSet.getDouble("donGia"),
                            loaiHD,
                            resultSet.getInt("soGio")
                    );
                }

                if (invoice != null) {
                    listInvoice.add(invoice);
                }
            }

        } catch (Exception e) {
            System.out.println("Lỗi: " + e.getMessage());
        } finally {
            close();
        }

        return listInvoice;
    }
}
