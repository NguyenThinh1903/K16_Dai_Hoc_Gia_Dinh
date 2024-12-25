package org.example.useCase.deleteInvoice;

import org.example.ui.deleteInvoice.DeleteInvoiceController;

import javax.swing.*;

public class DeleteInvoiceUIView {
    private DeleteInvoiceController deleteInvoiceController;

    public void mainShow(DeleteInvoiceUIViewModel deleteInvoiceUIViewModel){
        String maHD = deleteInvoiceUIViewModel.maHD;

        int resilt = JOptionPane.showConfirmDialog(null,
        "Bạn có muốn xoá hoá đơn "+ maHD+"không ",
                "Vui lòng chọn ",
                JOptionPane.YES_NO_OPTION);
        if (resilt == JOptionPane.YES_OPTION){
            DeleteInvoiceInputDTO deleteInvoiceInputDTO = new DeleteInvoiceInputDTO();
            deleteInvoiceInputDTO.setMaHD(maHD);
            deleteInvoiceController.execute(deleteInvoiceInputDTO);
        }
    }

    public void setDeleteInvoiceController(DeleteInvoiceController deleteInvoiceController) {
        this.deleteInvoiceController = deleteInvoiceController;
    }
}
