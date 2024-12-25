package org.example.ui.deleteInvoice;

import javax.swing.*;
import org.example.useCase.deleteInvoice.DeleteInvoiceInputDTO;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DeleteInvoiceView extends JFrame {
    private DeleteInvoiceController deleteInvoiceController;
    private JTextField tf_maHD;
    private JButton deleteInvoiceBtn, resetButton;
    private JPanel mainPanel;

    public DeleteInvoiceView() {

    }

//    private void build() {
//        // Thiết lập cơ bản cho frame
//        setTitle("Xóa hóa đơn khách sạn");
//        setSize(400, 300);
//        setResizable(false);
//        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//        setLayout(new BorderLayout(10, 10));
//
//        // Panel chính với padding
//        mainPanel = new JPanel(new BorderLayout(10, 10));
//        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
//
//        // Panel thông tin xóa hóa đơn
//        JPanel invoiceDetailsPanel = new JPanel(new GridLayout(2, 2, 10, 10));
//        invoiceDetailsPanel.setBorder(BorderFactory.createTitledBorder("Thông tin hóa đơn"));
//
//        // Mã hóa đơn
//        invoiceDetailsPanel.add(new JLabel("Mã hóa đơn:"));
//        tf_maHD = new JTextField();
//        invoiceDetailsPanel.add(tf_maHD);
//
//        // Panel cho các nút
//        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
//        deleteInvoiceBtn = new JButton("Delete Invoice");
//        resetButton = new JButton("Reset");
//
//        // Kích thước nút
//        Dimension buttonSize = new Dimension(120, 30);
//        deleteInvoiceBtn.setPreferredSize(buttonSize);
//        resetButton.setPreferredSize(buttonSize);
//
//        // Thêm nút vào button panel
//        buttonPanel.add(deleteInvoiceBtn);
//        buttonPanel.add(resetButton);
//
//        // Thêm các panel vào main panel
//        mainPanel.add(invoiceDetailsPanel, BorderLayout.CENTER);
//        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
//
//        // Thêm main panel vào frame
//        add(mainPanel);
//
//        // Xử lý sự kiện nút
//        deleteInvoiceBtn.addActionListener(e -> handleDeleteInvoice());
//        resetButton.addActionListener(e -> resetForm());
//
//        // Đặt frame ở giữa màn hình
//        setLocationRelativeTo(null);
//    }

//    private void handleDeleteInvoice() {
//        DeleteInvoiceInputDTO deleteInvoiceInputDTO = new DeleteInvoiceInputDTO();
//        deleteInvoiceInputDTO.setMaHD(tf_maHD.getText());
//
//        deleteInvoiceController.execute(deleteInvoiceInputDTO);
//    }

    private void resetForm() {
        tf_maHD.setText("");
    }

    public void showMsgResult(DeleteInvoiceViewModel deleteInvoiceViewModel) {
        JOptionPane.showMessageDialog(this,
                deleteInvoiceViewModel.msg,
                deleteInvoiceViewModel.status,
                JOptionPane.INFORMATION_MESSAGE);
    }

    public void showMsgError(DeleteInvoiceViewModel deleteInvoiceViewModel) {
        JOptionPane.showMessageDialog(this,
                deleteInvoiceViewModel.msg,
                deleteInvoiceViewModel.status,
                JOptionPane.ERROR_MESSAGE);
    }

    public void setDeleteInvoiceController(DeleteInvoiceController deleteInvoiceController) {
        this.deleteInvoiceController = deleteInvoiceController;
    }

    public void mainShow() {
        setVisible(true);
    }
}
