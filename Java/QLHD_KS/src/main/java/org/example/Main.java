package org.example;

import org.example.database.*;
import org.example.ui.addInvoice.*;
import org.example.ui.deleteInvoice.DeleteInvoiceController;
import org.example.ui.deleteInvoice.DeleteInvoicePresenter;
import org.example.ui.deleteInvoice.DeleteInvoiceView;
import org.example.ui.deleteInvoice.DeleteInvoiceViewModel;
import org.example.ui.editInvoice.EditInvoiceController;
import org.example.ui.editInvoice.EditInvoicePresenter;
import org.example.ui.editInvoice.EditInvoiceView;
import org.example.ui.editInvoice.EditInvoiceViewModel;
import org.example.ui.findInvoice.FindInvoiceController;
import org.example.ui.findInvoice.FindInvoicePresenter;
import org.example.ui.findInvoice.FindInvoiceView;
import org.example.ui.findInvoice.FindInvoiceViewModel;
import org.example.ui.getListInvoice.GetListInvoiceController;
import org.example.ui.getListInvoice.GetListInvoicePresenter;
import org.example.ui.getListInvoice.GetListInvoiceView;
import org.example.ui.getListInvoice.GetListInvoiceViewModel;
import org.example.ui.quanLyHDKhachSan.QuanLyHDKhachSanController;
import org.example.ui.quanLyHDKhachSan.QuanLyHDKhachSanPresenter;
import org.example.ui.quanLyHDKhachSan.QuanLyHDKhachSanView;
import org.example.ui.quanLyHDKhachSan.QuanLyHDKhachSanViewModel;
import org.example.useCase.addInvoice.*;
import org.example.useCase.deleteInvoice.*;
import org.example.useCase.editInvoice.*;
import org.example.useCase.findInvoice.*;
import org.example.useCase.getListInvoice.*;
import org.example.useCase.quanLyHoaDon_KS.QuanLyHDKhachSanInputBoundary;
import org.example.useCase.quanLyHoaDon_KS.QuanLyHDKhachSanInputDTO;
import org.example.useCase.quanLyHoaDon_KS.QuanLyHDKhachSanOutputBoundary;
import org.example.useCase.quanLyHoaDon_KS.QuanLyHDKhachSanUseCase;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        Class.forName("org.jdesktop.swingx.JXDatePicker");

        final String ip = "127.0.0.1";
        final int port = 3306;
        final  String db = "invoice";
        final String username = "root";
        final String password = "Scarlett 2003";

        //AddInvoice
        AddInvoiceViewModel addInvoiceViewModel = new AddInvoiceViewModel();
        AddInvoiceView addInvoiceView = new AddInvoiceView();
        AddInvoiceOutputBoundary addInvoiceOutputBoundary = new AddInvoicePresenter(addInvoiceView, addInvoiceViewModel);
        AddInvoiceDatabaseBoundary addInvoiceDatabaseBoundary = new AddInvoiceDAOMySQL(ip, port, db, username, password);
        AddInvoiceInputBoundary addInvoiceInputBoundary = new AddInvoiceUseCase(addInvoiceDatabaseBoundary, addInvoiceOutputBoundary);
        AddInvoiceController addInvoiceController = new AddInvoiceController(addInvoiceInputBoundary);
        addInvoiceView.setAddInvoiceController(addInvoiceController);

        AddInvoiceUIInputBoundary addInvoiceUIInputBoundary = new AddInvoiceUIUseCase(addInvoiceView);

        //EditInvoice
        EditInvoiceViewModel editInvoiceViewModel = new EditInvoiceViewModel();
        EditInvoiceView editInvoiceView = new EditInvoiceView();
        EditInvocieOutputBoundary editInvocieOutputBoundary = new EditInvoicePresenter(editInvoiceView, editInvoiceViewModel);
        EditInvoiceDatabaseBoundary editInvoiceDatabaseBoundary = new EditInvoiceDAOMySQL(ip, port, db, username, password);
        EditInvoiceInputBoundary editInvoiceInputBoundary = new EditInvoiceUseCase(editInvocieOutputBoundary, editInvoiceDatabaseBoundary);
        EditInvoiceController editInvoiceController = new EditInvoiceController(editInvoiceInputBoundary);
        editInvoiceView.setEditInvoiceController(editInvoiceController);
        EditInvoiceUIPresenter editInvoiceUIPresenter = new EditInvoiceUIPresenter(editInvoiceViewModel, editInvoiceView);

        EditInvoiceUIInputBoundary editInvoiceUIInputBoundary = new EditInvoiceUIUseCase(editInvoiceUIPresenter);
        //FindInvocie
        List<FindInvoiceViewModel> listFindInvoiceViewModel = new ArrayList<>();
        FindInvoiceView findInvoiceView = new FindInvoiceView();
        FindInvoiceOutputBoundary findInvoiceOutputBoundary = new FindInvoicePresenter(findInvoiceView, listFindInvoiceViewModel);
        FindInvoiceDatabaseBoundary findInvoiceDatabaseBoundary = new FindInvoiceDAOMySQL(ip, port, db, username, password);
        FindInvocieInputBoundary findInvoiceInputBoundary = new FindInvocieUseCase(findInvoiceDatabaseBoundary, findInvoiceOutputBoundary);
        FindInvoiceController findInvoiceController = new FindInvoiceController(findInvoiceInputBoundary);
        findInvoiceView.setFindInvoiceController(findInvoiceController);

        FindInvoiceUIInputBoundary findInvoiceUIInputBoundary = new FindInvoiceUIUseCase(findInvoiceView);

        List<GetListInvoiceViewModel> listGetListInvoiceViewModel = new ArrayList<>();
        GetListInvoiceView getListInvoiceView = new GetListInvoiceView();
        GetListInvoiceOutputBoundary getListInvoiceOutputBoundary = new GetListInvoicePresenter(getListInvoiceView, listGetListInvoiceViewModel);
        GetListInvoiceDatabaseBoundary getListInvoiceDatabaseBoundary = new GetListInvoiceDAOMySQL(ip, port, db, username, password);
        GetListInvoiceInputBoundary getListInvoiceInputBoundary = new GetListInvoiceUseCase(getListInvoiceOutputBoundary, getListInvoiceDatabaseBoundary);
        GetListInvoiceController getListInvoiceController = new GetListInvoiceController(getListInvoiceInputBoundary);
        getListInvoiceView.setGetListInvoiceController(getListInvoiceController);

        GetListInvoiceUIInputBoundary getListInvoiceUIInputBoundary = new GetListInvoiceUIUseCase(getListInvoiceView);

        DeleteInvoiceViewModel DeleteInvoiceViewModel = new DeleteInvoiceViewModel();
        DeleteInvoiceView deleteInvoiceView = new DeleteInvoiceView();
        DeleteInvoiceOutputBoundary deleteInvoiceOutputBoundary = new DeleteInvoicePresenter(deleteInvoiceView, DeleteInvoiceViewModel);
        DeleteInvoiceDatabaseBoundary deleteInvoiceDatabaseBoundary = new DeleteInvoiceDAOMySQL(ip, port, db, username, password);
        DeleteInvoiceInputBoundary deleteInvoiceInputBoundary = new DeleteInvoiceUseCase(deleteInvoiceOutputBoundary, deleteInvoiceDatabaseBoundary);
        DeleteInvoiceController deleteInvoiceController = new DeleteInvoiceController(deleteInvoiceInputBoundary);
        deleteInvoiceView.setDeleteInvoiceController(deleteInvoiceController);

        DeleteInvoiceUIView deleteInvoiceUIView = new DeleteInvoiceUIView();
        DeleteInvoiceUIViewModel deleteInvoiceUIViewModel = new DeleteInvoiceUIViewModel();
        deleteInvoiceUIView.setDeleteInvoiceController(deleteInvoiceController);
        DeleteInvoiceUIPresenter deleteInvoiceUIPresenter = new DeleteInvoiceUIPresenter(deleteInvoiceUIView, deleteInvoiceUIViewModel);
        DeleteInvoiceUIInputBoundary deleteInvoiceUIInputBoundary = new DeleteInvoiceUIUseCase(deleteInvoiceUIPresenter);



        //GUI
        QuanLyHDKhachSanViewModel quanLyHDKhachSanViewModel = new QuanLyHDKhachSanViewModel();
        QuanLyHDKhachSanView quanLyHDKhachSanView = new QuanLyHDKhachSanView();
        QuanLyHDKhachSanOutputBoundary quanLyHDKhachSanOutputBoundary = new QuanLyHDKhachSanPresenter(quanLyHDKhachSanViewModel, quanLyHDKhachSanView );
        QuanLyHDKhachSanInputBoundary quanLyHDKhachSanInputBoundary = new QuanLyHDKhachSanUseCase(quanLyHDKhachSanOutputBoundary);
        QuanLyHDKhachSanController quanLyHDKhachSanController = new QuanLyHDKhachSanController(quanLyHDKhachSanInputBoundary);

        quanLyHDKhachSanView.setListInvoice(listGetListInvoiceViewModel);
        quanLyHDKhachSanView.setGetListInvoiceInputBoundary(getListInvoiceInputBoundary);
        quanLyHDKhachSanView.setQuanLyHDKhachSanController(quanLyHDKhachSanController);
        quanLyHDKhachSanInputBoundary.setAddInvoiceUIInputBoundary(addInvoiceUIInputBoundary);
        quanLyHDKhachSanInputBoundary.setDeleteInvoiceUIInputBoundary(deleteInvoiceUIInputBoundary);
        quanLyHDKhachSanInputBoundary.SetFindInvoiceUIInputBoundary(findInvoiceUIInputBoundary);
        quanLyHDKhachSanInputBoundary.setEditInvoiceUIInputBoundary(editInvoiceUIInputBoundary);
//        quanLyHDKhachSanInputBoundary.setGetListInvoiceUIInputBoundary(getListInvoiceUIInputBoundary);
        quanLyHDKhachSanInputBoundary.setGetListInvoiceUIInputBoundary(getListInvoiceUIInputBoundary);
        //setVisible(true)

        ((AddInvoiceUseCase)addInvoiceInputBoundary).subscriber(quanLyHDKhachSanView);
        ((DeleteInvoiceUseCase)deleteInvoiceInputBoundary).subscriber(quanLyHDKhachSanView);
        ((EditInvoiceUseCase)editInvoiceInputBoundary).subscriber(quanLyHDKhachSanView);
        quanLyHDKhachSanController.execute(null);
        quanLyHDKhachSanView.showGui();
    }

}