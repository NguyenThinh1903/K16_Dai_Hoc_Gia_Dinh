package org.example.database;

import org.example.useCase.deleteInvoice.DeleteInvoiceDatabaseBoundary;

import java.sql.PreparedStatement;

public class DeleteInvoiceDAOMySQL extends DAOMySQL implements DeleteInvoiceDatabaseBoundary {

    public DeleteInvoiceDAOMySQL(String ipAddress, int port, String database, String username, String password) throws Exception {
        super(ipAddress, port, database, username, password);
    }

    @Override
    public void deleteInvoice(int maHD) {
        connect();

        String sql = "DELETE FROM invoice WHERE (maHD = ?)";

        try {
            PreparedStatement preparedStatement = getPrepareStatement(sql);
            preparedStatement.setInt(1, maHD);

            preparedStatement.execute();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        close();
    }
}
