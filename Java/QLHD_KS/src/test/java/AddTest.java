import org.example.database.AddInvoiceDAOMySQL;
import org.example.ui.addInvoice.AddInvoiceController;
import org.example.ui.addInvoice.AddInvoicePresenter;
import org.example.ui.addInvoice.AddInvoiceViewModel;
import org.example.useCase.addInvoice.*;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AddTest extends NhomTest{
    private AddInvoiceInputDTO getRequestData() {
        AddInvoiceInputDTO addInvoiceInputDTO = new AddInvoiceInputDTO();

        addInvoiceInputDTO.setTenKH("tênTest");
        addInvoiceInputDTO.setNgayHoaDon("2023-12-30");
        addInvoiceInputDTO.setMaPhong("h12");
        addInvoiceInputDTO.setDonGia("150.00");
        addInvoiceInputDTO.setLoaiHoaDon("Theo ngày");  // Ensure the case matches with use case
        addInvoiceInputDTO.setSoNgay("2");

        return addInvoiceInputDTO;
    }

    @Test
    public void addInvoiceSuccess() throws Exception {
        AddInvoiceViewModel addInvoiceViewModel = new AddInvoiceViewModel();
        AddInvoiceOutputBoundary addInvoiceOutputBoundary = new AddInvoicePresenter(null, addInvoiceViewModel);
        AddInvoiceDatabaseBoundary addInvoiceDatabaseBoundary = new AddInvoiceDAOMySQL(ipAddress, port, db, username, password);
        AddInvoiceInputBoundary addInvoiceInputBoundary = new AddInvoiceUseCase( addInvoiceDatabaseBoundary, addInvoiceOutputBoundary);
        AddInvoiceController addInvoiceController = new AddInvoiceController(addInvoiceInputBoundary);

        AddInvoiceInputDTO addInvoiceInputDTO = getRequestData();
        addInvoiceController.execute(addInvoiceInputDTO);

        System.out.println(addInvoiceViewModel.message);
        assertEquals("Đã thêm thành công!", addInvoiceViewModel.message);
    }

    @Test
    public void addInvoiceErrorInvalidDate() throws Exception {
        AddInvoiceViewModel addInvoiceViewModel = new AddInvoiceViewModel();
        AddInvoiceOutputBoundary addInvoiceOutputBoundary = new AddInvoicePresenter(null, addInvoiceViewModel);
        AddInvoiceDatabaseBoundary addInvoiceDatabaseBoundary = new AddInvoiceDAOMySQL(ipAddress, port, db, username, password);
        AddInvoiceInputBoundary addInvoiceInputBoundary = new AddInvoiceUseCase(addInvoiceDatabaseBoundary, addInvoiceOutputBoundary);
        AddInvoiceController addInvoiceController = new AddInvoiceController(addInvoiceInputBoundary);

        AddInvoiceInputDTO requestData = getRequestData();
        requestData.setNgayHoaDon("20241705");  // Invalid date format

        addInvoiceController.execute(requestData);

        System.out.println(addInvoiceViewModel.message);
        assertEquals("Ngày hóa đơn sai định dạng", addInvoiceViewModel.message);
    }

    @Test
    public void addInvoiceErrorEmptyCustomerName() throws Exception {
        AddInvoiceViewModel addInvoiceViewModel = new AddInvoiceViewModel();
        AddInvoiceOutputBoundary addInvoiceOutputBoundary = new AddInvoicePresenter(null, addInvoiceViewModel);
        AddInvoiceDatabaseBoundary addInvoiceDatabaseBoundary = new AddInvoiceDAOMySQL(ipAddress, port, db, username, password);
        AddInvoiceInputBoundary addInvoiceInputBoundary = new AddInvoiceUseCase( addInvoiceDatabaseBoundary, addInvoiceOutputBoundary);
        AddInvoiceController addInvoiceController = new AddInvoiceController(addInvoiceInputBoundary);

        AddInvoiceInputDTO requestData = getRequestData();
        requestData.setTenKH("");  // Empty customer name

        addInvoiceController.execute(requestData);

        System.out.println(addInvoiceViewModel.message);
        assertEquals("Tên khách hàng không được để trống", addInvoiceViewModel.message);
    }

    @Test
    public void addInvoiceErrorInvalidRoomCode() throws Exception {
        AddInvoiceViewModel addInvoiceViewModel = new AddInvoiceViewModel();
        AddInvoiceOutputBoundary addInvoiceOutputBoundary = new AddInvoicePresenter(null, addInvoiceViewModel);
        AddInvoiceDatabaseBoundary addInvoiceDatabaseBoundary = new AddInvoiceDAOMySQL(ipAddress, port, db, username, password);
        AddInvoiceInputBoundary addInvoiceInputBoundary = new AddInvoiceUseCase(addInvoiceDatabaseBoundary, addInvoiceOutputBoundary);
        AddInvoiceController addInvoiceController = new AddInvoiceController(addInvoiceInputBoundary);

        AddInvoiceInputDTO requestData = getRequestData();
        requestData.setMaPhong("");  // Empty room code

        addInvoiceController.execute(requestData);

        System.out.println(addInvoiceViewModel.message);
        assertEquals("Mã phòng không được để trống", addInvoiceViewModel.message);
    }

    @Test
    public void addInvoiceErrorInvalidPrice() throws Exception {
        AddInvoiceViewModel addInvoiceViewModel = new AddInvoiceViewModel();
        AddInvoiceOutputBoundary addInvoiceOutputBoundary = new AddInvoicePresenter(null, addInvoiceViewModel);
        AddInvoiceDatabaseBoundary addInvoiceDatabaseBoundary = new AddInvoiceDAOMySQL(ipAddress, port, db, username, password);
        AddInvoiceInputBoundary addInvoiceInputBoundary = new AddInvoiceUseCase(addInvoiceDatabaseBoundary, addInvoiceOutputBoundary);
        AddInvoiceController addInvoiceController = new AddInvoiceController(addInvoiceInputBoundary);

        AddInvoiceInputDTO requestData = getRequestData();
        requestData.setDonGia("-100");  // Invalid price

        addInvoiceController.execute(requestData);

        System.out.println(addInvoiceViewModel.message);
        assertEquals("Đơn giá phải lớn hơn 0", addInvoiceViewModel.message);
    }

    @Test
    public void addInvoiceErrorEmptyInvoiceType() throws Exception {
        AddInvoiceViewModel addInvoiceViewModel = new AddInvoiceViewModel();
        AddInvoiceOutputBoundary addInvoiceOutputBoundary = new AddInvoicePresenter(null, addInvoiceViewModel);
        AddInvoiceDatabaseBoundary addInvoiceDatabaseBoundary = new AddInvoiceDAOMySQL(ipAddress, port, db, username, password);
        AddInvoiceInputBoundary addInvoiceInputBoundary = new AddInvoiceUseCase(addInvoiceDatabaseBoundary, addInvoiceOutputBoundary);
        AddInvoiceController addInvoiceController = new AddInvoiceController(addInvoiceInputBoundary);

        AddInvoiceInputDTO requestData = getRequestData();
        requestData.setLoaiHoaDon("");  // Empty invoice type

        addInvoiceController.execute(requestData);

        System.out.println(addInvoiceViewModel.message);
        assertEquals("Loại hóa đơn không được để trống", addInvoiceViewModel.message);
    }

}