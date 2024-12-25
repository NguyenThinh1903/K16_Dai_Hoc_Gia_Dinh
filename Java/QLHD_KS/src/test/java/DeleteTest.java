import org.example.database.DeleteInvoiceDAOMySQL;
import org.example.ui.deleteInvoice.DeleteInvoiceController;
import org.example.ui.deleteInvoice.DeleteInvoicePresenter;
import org.example.ui.deleteInvoice.DeleteInvoiceViewModel;

import org.example.useCase.deleteInvoice.*;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DeleteTest extends NhomTest{
    private DeleteInvoiceInputDTO getRequestData() {
        DeleteInvoiceInputDTO requestData = new DeleteInvoiceInputDTO();
        requestData.setMaHD("5");
        return requestData;
    }

    @Test
    public void deleteInvoiceSuccess() throws Exception {
        DeleteInvoiceViewModel deleteInvoiceViewModel = new DeleteInvoiceViewModel();
        DeleteInvoiceOutputBoundary deleteInvoiceOutputBoundary = new DeleteInvoicePresenter(null, deleteInvoiceViewModel);
        DeleteInvoiceDatabaseBoundary deleteInvoiceDatabaseBoundary = new DeleteInvoiceDAOMySQL(ipAddress, port, db, username, password); // Giả lập cho hóa đơn tồn tại
        DeleteInvoiceInputBoundary deleteInvoiceInputBoundary = new DeleteInvoiceUseCase(deleteInvoiceOutputBoundary, deleteInvoiceDatabaseBoundary);
        DeleteInvoiceController deleteInvoiceController = new DeleteInvoiceController(deleteInvoiceInputBoundary);

        DeleteInvoiceInputDTO requestData = getRequestData();

        deleteInvoiceController.execute(requestData);

        System.out.println(deleteInvoiceViewModel.msg);
        assertEquals("Đã xóa thành công!", deleteInvoiceViewModel.msg);
    }

    @Test
    public void deleteInvoiceInvalidInput() throws Exception {
        DeleteInvoiceViewModel deleteInvoiceViewModel = new DeleteInvoiceViewModel();
        DeleteInvoiceOutputBoundary deleteInvoiceOutputBoundary = new DeleteInvoicePresenter(null, deleteInvoiceViewModel);
        DeleteInvoiceDatabaseBoundary deleteInvoiceDatabaseBoundary = new DeleteInvoiceDAOMySQL(ipAddress, port, db, username, password);
        DeleteInvoiceInputBoundary deleteInvoiceInputBoundary = new DeleteInvoiceUseCase(deleteInvoiceOutputBoundary, deleteInvoiceDatabaseBoundary);
        DeleteInvoiceController deleteInvoiceController = new DeleteInvoiceController(deleteInvoiceInputBoundary);

        DeleteInvoiceInputDTO requestData = getRequestData();
        requestData.setMaHD("ID08");
        deleteInvoiceController.execute(requestData);

        System.out.println(deleteInvoiceViewModel.msg);
        assertEquals("Mã hóa đơn không được chứa chữ", deleteInvoiceViewModel.msg);
    }

    @Test
    public void deleteInvoiceNoDatabase() throws Exception {
        DeleteInvoiceViewModel deleteInvoiceViewModel = new DeleteInvoiceViewModel();
        DeleteInvoiceOutputBoundary deleteInvoiceOutputBoundary = new DeleteInvoicePresenter(null, deleteInvoiceViewModel);
        DeleteInvoiceDatabaseBoundary deleteInvoiceDatabaseBoundary = new DeleteInvoiceDAOMySQL(ipAddress, port, db, username, password);
        DeleteInvoiceInputBoundary deleteInvoiceInputBoundary = new DeleteInvoiceUseCase(deleteInvoiceOutputBoundary, deleteInvoiceDatabaseBoundary);
        DeleteInvoiceController deleteInvoiceController = new DeleteInvoiceController(deleteInvoiceInputBoundary);

        DeleteInvoiceInputDTO requestData = getRequestData();
        requestData.setMaHD("0");
        deleteInvoiceController.execute(requestData);

        System.out.println(deleteInvoiceViewModel.msg);
        assertEquals("Không tồn tại!", deleteInvoiceViewModel.msg);
    }


}