package org.example.ui.editInvoice;

import javax.swing.*;

import org.example.useCase.editInvoice.EditInvoiceInputDTO;
import org.jdesktop.swingx.JXDatePicker;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class EditInvoiceView extends JFrame {
    private JTextField tf_maHD, tf_TenKH, tf_MaPhong, tf_DonGia, tf_SoNgay, tf_SoGio, tf_thanhTien;
    private JComboBox<String> cb_LoaiHoaDon;
    private JXDatePicker dp_NgayHD;
    private JButton updateInvoiceBtn, resetButton;
    private JPanel invoiceDetailsPanel, dynamicPanel;
    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    private EditInvoiceController editInvoiceController;
    public EditInvoiceView() {
//        build();
    }

    private void build() {
        getContentPane().removeAll();
        // Basic frame setup
        setTitle("Edit Invoice Form");
        setSize(650, 600); // Điều chỉnh kích thước hợp lý hơn
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Main content panel with padding
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Panel for common invoice details
        invoiceDetailsPanel = new JPanel(new GridLayout(7, 2, 10, 15)); // Tăng khoảng cách hàng và cột
        invoiceDetailsPanel.setBorder(BorderFactory.createTitledBorder("Invoice Information"));

        invoiceDetailsPanel.add(new JLabel("Mã Hoá đơn:"));
        tf_maHD = new JTextField();
        tf_maHD.setEditable(false); // Không cho phép chỉnh sửa
        invoiceDetailsPanel.add(tf_maHD);

        invoiceDetailsPanel.add(new JLabel("Tên khách hàng:"));
        tf_TenKH = new JTextField();
        invoiceDetailsPanel.add(tf_TenKH);

        invoiceDetailsPanel.add(new JLabel("Ngày hóa đơn:"));
        dp_NgayHD = new JXDatePicker();
        dp_NgayHD.setDate(Calendar.getInstance().getTime());
        dp_NgayHD.setFormats(formatter);
        invoiceDetailsPanel.add(dp_NgayHD);

        invoiceDetailsPanel.add(new JLabel("Mã phòng:"));
        tf_MaPhong = new JTextField();
        invoiceDetailsPanel.add(tf_MaPhong);

        invoiceDetailsPanel.add(new JLabel("Đơn giá:"));
        tf_DonGia = new JTextField();
        invoiceDetailsPanel.add(tf_DonGia);

        invoiceDetailsPanel.add(new JLabel("Loại hóa đơn:"));
        String[] options = {"", "Theo giờ", "Theo ngày"};
        cb_LoaiHoaDon = new JComboBox<>(options);
        invoiceDetailsPanel.add(cb_LoaiHoaDon);

        invoiceDetailsPanel.add(new JLabel("Thành tiền:"));
        tf_thanhTien = new JTextField();
        tf_thanhTien.setEditable(false); // Không cho phép chỉnh sửa
        invoiceDetailsPanel.add(tf_thanhTien);

        // Dynamic panel for hourly/daily inputs
        dynamicPanel = new JPanel(new CardLayout());
        dynamicPanel.setBorder(BorderFactory.createTitledBorder("Chi tiết theo loại hóa đơn"));

        // Panel for daily rate
        JPanel dailyPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        dailyPanel.setBorder(BorderFactory.createTitledBorder("Thông tin theo ngày"));
        dailyPanel.add(new JLabel("Số ngày:"));
        tf_SoNgay = new JTextField();
        dailyPanel.add(tf_SoNgay);

        // Panel for hourly rate
        JPanel hourlyPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        hourlyPanel.setBorder(BorderFactory.createTitledBorder("Thông tin theo giờ"));
        hourlyPanel.add(new JLabel("Số giờ:"));
        tf_SoGio = new JTextField();
        hourlyPanel.add(tf_SoGio);

        // Add panels to dynamic panel
        dynamicPanel.add(new JPanel(), "");
        dynamicPanel.add(hourlyPanel, "Theo giờ");
        dynamicPanel.add(dailyPanel, "Theo ngày");

        // Invoice type selection handler
        cb_LoaiHoaDon.addActionListener(e -> {
            String selectedType = (String) cb_LoaiHoaDon.getSelectedItem();
            CardLayout cl = (CardLayout) (dynamicPanel.getLayout());
            cl.show(dynamicPanel, selectedType);
        });

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        updateInvoiceBtn = new JButton("Cập nhật");
        resetButton = new JButton("Làm mới");

        // Style buttons
        Dimension buttonSize = new Dimension(140, 40); // Nút lớn hơn để rõ ràng hơn
        updateInvoiceBtn.setPreferredSize(buttonSize);
        resetButton.setPreferredSize(buttonSize);

        buttonPanel.add(updateInvoiceBtn);
        buttonPanel.add(resetButton);
        updateInvoiceBtn.addActionListener(e -> handleUpdateInvoice());
        resetButton.addActionListener(e ->resetButton());
        // Add panels to main panel
        mainPanel.add(invoiceDetailsPanel, BorderLayout.NORTH);
        mainPanel.add(dynamicPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Add main panel to frame
        add(mainPanel);

        // Center the frame on screen
        setLocationRelativeTo(null);
    }


    private void handleUpdateInvoice() {
        EditInvoiceInputDTO editInvoiceInputDTO = new EditInvoiceInputDTO();
        editInvoiceInputDTO.setMaHD(tf_maHD.getText());
        editInvoiceInputDTO.setTenKH(tf_TenKH.getText());
        editInvoiceInputDTO.setNgayHD(formatter.format(dp_NgayHD.getDate()));
        editInvoiceInputDTO.setDonGia(tf_DonGia.getText());
        editInvoiceInputDTO.setLoaiHoaDon(cb_LoaiHoaDon.getSelectedItem().toString());
        editInvoiceInputDTO.setSoNgay(tf_SoNgay.getText());
        editInvoiceInputDTO.setSoGio(tf_SoGio.getText());
        editInvoiceInputDTO.setMaPhong(tf_MaPhong.getText());
        editInvoiceController.execute(editInvoiceInputDTO);
    }
    public void resetButton(){
        // Đặt lại tất cả các trường văn bản
        tf_maHD.setText("");
        tf_TenKH.setText("");
        dp_NgayHD.setDate(Calendar.getInstance().getTime());
        tf_MaPhong.setText("");
        tf_DonGia.setText("");
        cb_LoaiHoaDon.setSelectedIndex(0); // Đặt ComboBox về lựa chọn đầu tiên
        tf_SoNgay.setText("");
        tf_SoGio.setText("");
        tf_thanhTien.setText("");

        // Ẩn panel chi tiết hóa đơn động
        CardLayout cl = (CardLayout) dynamicPanel.getLayout();
        cl.show(dynamicPanel, "");

    }
    public void showMsgError(EditInvoiceViewModel editInvoiceViewModel){
        JOptionPane.showMessageDialog(this,
                editInvoiceViewModel.message,
                editInvoiceViewModel.status,
                JOptionPane.ERROR_MESSAGE);
    }
    public void showMsgResult(EditInvoiceViewModel editInvoiceViewModel){
        JOptionPane.showMessageDialog(this,
                editInvoiceViewModel.status,
                editInvoiceViewModel.message,
                JOptionPane.INFORMATION_MESSAGE);
    }

//    private void resetForm() {
//        // Khôi phục giá trị của form
//        tf_TenKH.setText("");
//        dp_NgayHD.setDate(Calendar.getInstance().getTime());
//        tf_MaPhong.setText("");
//        tf_DonGia.setText("");
//        cb_LoaiHoaDon.setSelectedIndex(0);
//        tf_SoNgay.setText("");
//        tf_SoGio.setText("");
//    }

    public void setEditInvoiceController(EditInvoiceController editInvoiceController) {
        this.editInvoiceController = editInvoiceController;
    }

    public void mainShow() {
        setVisible(true);
    }
    public void showInvocie(EditInvoiceViewModel editInvoiceViewModel){
        String maHD = editInvoiceViewModel.maHD;
        String tenKH =editInvoiceViewModel.tenKH;
        String ngayHD = editInvoiceViewModel.ngayHD;
        String maPhong = editInvoiceViewModel.maPhong;
        String donGia = editInvoiceViewModel.donGia;
        String loaiHoaDon = editInvoiceViewModel.loaiHoaDon;
        String soNgay = editInvoiceViewModel.soNgay;
        String soGio = editInvoiceViewModel.soGio;
        String thanhTien = editInvoiceViewModel.thanhTien;
        build();
        tf_maHD.setText(maHD);
        tf_TenKH.setText(tenKH);
        try {
            dp_NgayHD.setDate(formatter.parse(ngayHD));
        }catch (Exception e){
            e.printStackTrace();
        }
        tf_MaPhong.setText(maPhong);
        tf_DonGia.setText(donGia);
        cb_LoaiHoaDon.setSelectedItem(loaiHoaDon);
        if (loaiHoaDon.equals("Theo ngày")){
            tf_SoNgay.setText(soNgay);
        } else if (loaiHoaDon.equals("Theo giờ")){
            tf_SoGio.setText(soGio);
        } else {
            JOptionPane.showMessageDialog(this,
                    "Hoá đơn không hợp lệ",
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
        }
        tf_thanhTien.setText(thanhTien);
        setVisible(true);
    }

}
