package org.example.useCase.deleteInvoice;

import org.example.ui.observer.Publisher;

public class DeleteInvoiceUseCase extends Publisher implements DeleteInvoiceInputBoundary {
    private DeleteInvoiceOutputBoundary deleteInvoiceOutputBoundary;
    private DeleteInvoiceDatabaseBoundary deleteInvoiceDatabaseBoundary;

    public DeleteInvoiceUseCase(DeleteInvoiceOutputBoundary deleteInvoiceOutputBoundary, DeleteInvoiceDatabaseBoundary deleteInvoiceDatabaseBoundary) {
        this.deleteInvoiceOutputBoundary = deleteInvoiceOutputBoundary;
        this.deleteInvoiceDatabaseBoundary = deleteInvoiceDatabaseBoundary;
    }

    private Boolean isExist(int maHD) {
        return this.deleteInvoiceDatabaseBoundary.isExist(maHD);
    }

    @Override
    public void execute(DeleteInvoiceInputDTO deleteInvoiceInputDTO) {
        DeleteInvoiceOutputDTO responseError = new DeleteInvoiceOutputDTO();
        responseError.setStatus("error");

        if (!verify(deleteInvoiceInputDTO)) {
            responseError.setMsg("Mã hóa đơn không được chứa chữ");
            deleteInvoiceOutputBoundary.exportError(responseError);
            return;
        }

        int maHD = Integer.parseInt(deleteInvoiceInputDTO.getMaHD());

        Boolean isE = this.isExist(maHD);
        if (isE == null) {
            responseError.setMsg("Đã xảy ra lỗi tại Database!");
            deleteInvoiceOutputBoundary.exportError(responseError);
            return;
        } else if (!isE) {
            responseError.setMsg("Không tồn tại!");
//            (HD: " + deleteInvoiceInputDTO.getMaHD() + ")
            deleteInvoiceOutputBoundary.exportError(responseError);
            return;
        }

        deleteInvoiceDatabaseBoundary.deleteInvoice(maHD);

        DeleteInvoiceOutputDTO outputDTO = new DeleteInvoiceOutputDTO();
        outputDTO.setStatus("error");
        outputDTO.setMsg("Đã xóa thành công!");
//        (HD: " + deleteInvoiceInputDTO.getMaHD() + ")
        deleteInvoiceOutputBoundary.present(outputDTO);
        notifySubscribers();
    }

    private boolean verify(DeleteInvoiceInputDTO deleteInvoiceInputDTO) {
        try {
            int maHD = Integer.parseInt(deleteInvoiceInputDTO.getMaHD());

        } catch (Exception ex) {
            return false;
        }

        return true;
    }

}
