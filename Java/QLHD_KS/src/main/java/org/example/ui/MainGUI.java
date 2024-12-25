//package org.example.ui;
//
//import org.example.ui.addInvoice.AddInvoiceView;
//import org.example.ui.deleteInvoice.DeleteInvoiceView;
//import org.example.ui.findInvoice.FindInvoiceView;
//import org.example.ui.getListInvoice.GetListInvoiceView;
//
//import javax.swing.*;
//import java.awt.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//
//public class MainGUI extends JFrame implements ActionListener {
//    private JButton addInvoiceBtn, deleteInvoiceBtn, editInvoiceBtn, exportAllInvoiceBtn, findInvoiceBtn;
//    private AddInvoiceView addInvoiceView;
//    private DeleteInvoiceView deleteInvoiceView;
////    private EditInvoiceView editInvoiceView;
//    private GetListInvoiceView getListInvoiceView;
//    private FindInvoiceView findInvoiceView;
//
//    public MainGUI() {
//        setTitle("Quản lý hóa đơn khách sạn");
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        setResizable(false);
//        setSize(500, 200);
//        setLayout(new BorderLayout());
//
//        // Create title panel
//        JPanel titlePanel = new JPanel();
//        titlePanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
//
//        // Create title label with blue color
//        JLabel titleLabel = new JLabel("QUẢN LÝ HÓA ĐƠN KHÁCH SẠN", SwingConstants.CENTER);
//        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
//        titleLabel.setForeground(Color.BLUE);
//        titlePanel.add(titleLabel);
//
//        // Create button panel
//        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
//
//        // Create buttons
//        addInvoiceBtn = new JButton("Thêm");
//        deleteInvoiceBtn = new JButton("Xóa");
//        editInvoiceBtn = new JButton("Sửa");
//        exportAllInvoiceBtn = new JButton("Xuất toàn bộ");
//        findInvoiceBtn = new JButton("Tìm kiếm");
//
//        // Add ActionListener
//        addInvoiceBtn.addActionListener(this);
//        deleteInvoiceBtn.addActionListener(this);
//        editInvoiceBtn.addActionListener(this);
//        exportAllInvoiceBtn.addActionListener(this);
//        findInvoiceBtn.addActionListener(this);
//
//        // Add buttons to button panel
//        buttonPanel.add(addInvoiceBtn);
//        buttonPanel.add(deleteInvoiceBtn);
//        buttonPanel.add(editInvoiceBtn);
//        buttonPanel.add(exportAllInvoiceBtn);
//        buttonPanel.add(findInvoiceBtn);
//
//        // Add panels to frame
//        add(titlePanel, BorderLayout.NORTH);
//        add(buttonPanel, BorderLayout.CENTER);
//
//        // Center the frame on screen
//        setLocationRelativeTo(null);
//    }
//
//    public void showGUI() {
//        setVisible(true);
//    }
//
//    public void setAddInvoiceView(AddInvoiceView addInvoiceView) {
//        this.addInvoiceView = addInvoiceView;
//    }
//
//    public void setDeleteInvoiceView(DeleteInvoiceView deleteInvoiceView) {
//        this.deleteInvoiceView = deleteInvoiceView;
//    }
////
////    public void setEditInvoiceView(EditInvoiceView editInvoiceView) {
////        this.editInvoiceView = editInvoiceView;
////    }
////
//    public void setGetListInvoiceView(GetListInvoiceView getListInvoiceView) {
//        this.getListInvoiceView = getListInvoiceView;
//    }
//
//    public void setFindInvoiceView(FindInvoiceView findInvoiceView) {
//        this.findInvoiceView = findInvoiceView;
//    }
//
//    @Override
//    public void actionPerformed(ActionEvent e) {
//        String cmd = e.getActionCommand();
//
//        if (cmd.equals(addInvoiceBtn.getActionCommand())) {
//            if (addInvoiceView != null) {
//                addInvoiceView.mainShow();
//            }
//        }
//
//        if (cmd.equals(deleteInvoiceBtn.getActionCommand())) {
//            if (deleteInvoiceView != null) {
//                deleteInvoiceView.mainShow();
//            }
//        }
////
////        if (cmd.equals(editInvoiceBtn.getActionCommand())) {
////            if (editInvoiceView != null) {
////                editInvoiceView.mainShow();
////            }
////        }
////
//        if (cmd.equals(exportAllInvoiceBtn.getActionCommand())) {
//            if (getListInvoiceView != null) {
//                getListInvoiceView.mainShow();
//            }
//        }
////
//        if (cmd.equals(findInvoiceBtn.getActionCommand())) {
//            if (findInvoiceView != null) {
//                findInvoiceView.mainShow();
//            }
//        }
//    }
//}