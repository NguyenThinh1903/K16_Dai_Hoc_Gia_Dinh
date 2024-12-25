package org.example.ui.quanLyHDKhachSan;

import org.example.entity.Invoice;
import org.example.entity.InvoiceGio;
import org.example.entity.InvoiceNgay;
import org.example.ui.getListInvoice.GetListInvoicePresenter;
import org.example.ui.getListInvoice.GetListInvoiceView;
import org.example.ui.getListInvoice.GetListInvoiceViewModel;
import org.example.ui.observer.Subscriber;
import org.example.useCase.getListInvoice.GetListInvoiceInputBoundary;
import org.example.useCase.quanLyHoaDon_KS.QuanLyHDKhachSanInputDTO;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QuanLyHDKhachSanView extends JFrame implements ActionListener, Subscriber {
    JButton addInvoice, deleteInvoice, editInvoice, findInvoice, getListInvoice, reloadValue;
    QuanLyHDKhachSanController quanLyHDKhachSanController;
    private int selectedRow = -1;
    private List<GetListInvoiceViewModel> listInvoice;
    private GetListInvoiceInputBoundary getListInvoiceInputBoundary;
    JTable invoiceTable;
    DefaultTableModel tableModel;

    public QuanLyHDKhachSanView(){
        setTitle("Quản lý hoá đơn khách sạn ");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLayout(new BorderLayout());
        setSize(1000,500);

        //create title panel
        JPanel titlePanel = new JPanel();
        titlePanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));

        JLabel titleLable = new JLabel("QUẢN LÝ HOÁ ĐƠN KHÁCH SẠN");
        titleLable.setFont(new Font("Arial", Font.BOLD, 30));
        titleLable.setForeground(Color.BLUE);
        titlePanel.add(titleLable);

        //danh sách hoá đơn
        JPanel listPanel = new JPanel(new BorderLayout());
        String[] columns = {
                "Mã HD", "Tên KH", "Ngày HD", "Mã phòng", "Đơn giá", "Loại HD", "Số ngày", "Số giờ", "Thành tiền"
        };
        //tạo model cho bảng và gắn với jTable
        tableModel = new DefaultTableModel(columns, 0);
        invoiceTable = new JTable(tableModel);

        //cho bảng vào trong JScrollPanel để hổ trợ cuộn
        JScrollPane scrollPane = new JScrollPane(invoiceTable);
        listPanel.add(scrollPane, BorderLayout.CENTER);
        //create panel button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        //create buttons
        addInvoice = new JButton("Thêm");
        deleteInvoice = new JButton("Xoá");
        findInvoice = new JButton("Tìm kiếm");
        editInvoice = new JButton("Sửa");
        getListInvoice = new JButton("Xuất");
        reloadValue = new JButton("làm mới");



        //action button
        addInvoice.addActionListener(this);
        deleteInvoice.addActionListener(this);
        findInvoice.addActionListener(this);
        getListInvoice.addActionListener(this);
        reloadValue.addActionListener(this);
        editInvoice.addActionListener(this);

        buttonPanel.add(addInvoice);
        buttonPanel.add(deleteInvoice);
        buttonPanel.add(findInvoice);
        buttonPanel.add(editInvoice);
        buttonPanel.add(getListInvoice);
        buttonPanel.add(reloadValue);

        add(titlePanel, BorderLayout.NORTH);
        add(listPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        setLocationRelativeTo(null);

    }

    public void showGui(){
        setVisible(true);
        this.getList();
    }

    public void setListInvoice(List<GetListInvoiceViewModel> listInvoice) {
        this.listInvoice = listInvoice;
    }

    public void setGetListInvoiceInputBoundary(GetListInvoiceInputBoundary getListInvoiceInputBoundary) {
        this.getListInvoiceInputBoundary = getListInvoiceInputBoundary;
    }

    public void setResultListInvoice(List<GetListInvoiceViewModel> listInvoice_){
        tableModel.setRowCount(0);
        for (GetListInvoiceViewModel ivoice: listInvoice_){
            Object[] row ={
                    ivoice.maHD,
                    ivoice.tenKH,
                    ivoice.ngayHD,
                    ivoice.maPhong,
                    ivoice.donGia,
                    ivoice.loaiHoaDon,
                    ivoice.soNgay,
                    ivoice.soGio,
                    ivoice.thanhTien
            };
            tableModel.addRow(row);
        }
    }

    public void getList(){
        if (this.getListInvoiceInputBoundary == null) {
            System.err.println("getListInvoiceInputBoundary chưa được khởi tạo! ");
            return;
        }
        this.getListInvoiceInputBoundary.execute();
        tableModel.setRowCount(0); // Xóa dữ liệu cũ trong bảng
        for (GetListInvoiceViewModel invoice : listInvoice) {
            Object[] row = {
                    invoice.maHD,
                    invoice.tenKH,
                    invoice.ngayHD,
                    invoice.maPhong,
                    invoice.donGia,
                    invoice.loaiHoaDon,
                    invoice.soNgay,
                    invoice.soGio,
                    invoice.thanhTien,
            };
            tableModel.addRow(row);
        }
    }
    public void setQuanLyHDKhachSanController(QuanLyHDKhachSanController quanLyHDKhachSanController) {
        this.quanLyHDKhachSanController = quanLyHDKhachSanController;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    String cmd = e.getActionCommand();

        if (cmd.equals(reloadValue.getText())){
            this.getList();
            return;
        }

        QuanLyHDKhachSanInputDTO quanLyHDKhachSanInputDTO = new QuanLyHDKhachSanInputDTO();
        selectedRow = invoiceTable.getSelectedRow();
        if (selectedRow == -1){
//            JOptionPane.showMessageDialog(null, "Vui lòng chọn một dòng trong bảng!", "Lỗi",
//                    JOptionPane.ERROR_MESSAGE);
        } else {
            String maHD = invoiceTable.getValueAt(selectedRow, 0).toString();
            String tenKH = invoiceTable.getValueAt(selectedRow, 1).toString();
            String ngayHD = invoiceTable.getValueAt(selectedRow,2).toString();
            String maPhong = invoiceTable.getValueAt(selectedRow, 3).toString();
            String donGia = invoiceTable.getValueAt(selectedRow, 4).toString();
            String loaiHoaDon = invoiceTable.getValueAt(selectedRow, 5).toString();
            String soNgay = invoiceTable.getValueAt(selectedRow, 6).toString();
            String soGio = invoiceTable.getValueAt(selectedRow, 7).toString();
            String thanhTien = invoiceTable.getValueAt(selectedRow, 8).toString();

            quanLyHDKhachSanInputDTO.setMaHD(maHD);
            quanLyHDKhachSanInputDTO.setTenKh(tenKH);
            quanLyHDKhachSanInputDTO.setNgayHD(ngayHD);
            quanLyHDKhachSanInputDTO.setMaPhong(maPhong);
            quanLyHDKhachSanInputDTO.setDonGia(donGia);
            quanLyHDKhachSanInputDTO.setLoatHoaDon(loaiHoaDon);
            quanLyHDKhachSanInputDTO.setSoNgay(soNgay);
            quanLyHDKhachSanInputDTO.setSoGio(soGio);
            quanLyHDKhachSanInputDTO.setThanhTien(thanhTien);

        }
            quanLyHDKhachSanInputDTO.setChucNang(cmd);
        quanLyHDKhachSanController.execute(quanLyHDKhachSanInputDTO);
    }

    public void showMesageError(QuanLyHDKhachSanViewModel quanLyHDKhachSanViewModel){
        JOptionPane.showMessageDialog(null,
                quanLyHDKhachSanViewModel.message,
                quanLyHDKhachSanViewModel.status,
                JOptionPane.ERROR_MESSAGE);
    }
    void showResult(QuanLyHDKhachSanViewModel quanLyHDKhachSanViewModel){
        return;
    }

    @Override
    public void update() {
        this.getList();
    }

//    @Override
//    public void valueChanged(ListSelectionEvent e) {
//        this.selectedRow = invoiceTable.getSelectedRow();
//    }
}
