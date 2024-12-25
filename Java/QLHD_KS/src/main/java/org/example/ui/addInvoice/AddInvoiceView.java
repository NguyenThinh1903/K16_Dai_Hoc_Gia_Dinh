package org.example.ui.addInvoice;

import javax.swing.*;
import org.example.useCase.addInvoice.AddInvoiceInputDTO;
import org.jdesktop.swingx.JXDatePicker;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddInvoiceView extends JFrame {
    private AddInvoiceController addInvoiceController;
    private JTextField tf_TenKH, tf_MaPhong, tf_DonGia, tf_SoNgay, tf_SoGio;
    private JComboBox<String> cb_LoaiHoaDon;
    private JXDatePicker dp_NgayHD;
    private JButton addInvoiceBtn, resetButton;
    private JPanel invoiceDetailsPanel, dynamicPanel;
    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    public AddInvoiceView() {
        build();
    }

    private void build() {
        // Basic frame setup
        setTitle("Add Invoice Form");
        setSize(500, 500);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Main content panel with padding
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Panel for common invoice details
        invoiceDetailsPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        invoiceDetailsPanel.setBorder(BorderFactory.createTitledBorder("Invoice Information"));

        // Customer name
        invoiceDetailsPanel.add(new JLabel("Tên khách hàng:"));
        tf_TenKH = new JTextField();
        invoiceDetailsPanel.add(tf_TenKH);

        // Invoice date
        invoiceDetailsPanel.add(new JLabel("Ngày hóa đơn:"));
        dp_NgayHD = new JXDatePicker();
        dp_NgayHD.setDate(Calendar.getInstance().getTime());
        dp_NgayHD.setFormats(formatter);
        invoiceDetailsPanel.add(dp_NgayHD);

        // Room number
        invoiceDetailsPanel.add(new JLabel("Mã phòng:"));
        tf_MaPhong = new JTextField();
        invoiceDetailsPanel.add(tf_MaPhong);

        // Price
        invoiceDetailsPanel.add(new JLabel("Đơn giá:"));
        tf_DonGia = new JTextField();
        invoiceDetailsPanel.add(tf_DonGia);

        // Invoice type
        invoiceDetailsPanel.add(new JLabel("Loại hóa đơn:"));
        String[] options = {"", "Theo giờ", "Theo ngày"};
        cb_LoaiHoaDon = new JComboBox<>(options);
        invoiceDetailsPanel.add(cb_LoaiHoaDon);

        // Dynamic panel for period details
        dynamicPanel = new JPanel(new CardLayout());
        dynamicPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

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
            CardLayout cl = (CardLayout)(dynamicPanel.getLayout());
            cl.show(dynamicPanel, selectedType);
        });

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        addInvoiceBtn = new JButton("Add Invoice");
        resetButton = new JButton("Reset");

        // Style buttons
        Dimension buttonSize = new Dimension(120, 30);
        addInvoiceBtn.setPreferredSize(buttonSize);
        resetButton.setPreferredSize(buttonSize);

        buttonPanel.add(addInvoiceBtn);
        buttonPanel.add(resetButton);

        // Add panels to main panel
        mainPanel.add(invoiceDetailsPanel, BorderLayout.NORTH);
        mainPanel.add(dynamicPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Add main panel to frame
        add(mainPanel);

        // Button listeners
        addInvoiceBtn.addActionListener(e -> handleAddInvoice());
        resetButton.addActionListener(e -> resetForm());

        // Center the frame on screen
        setLocationRelativeTo(null);
    }

    private void handleAddInvoice() {
        AddInvoiceInputDTO addInvoiceInputDTO = new AddInvoiceInputDTO();

        addInvoiceInputDTO.setTenKH(tf_TenKH.getText());
        addInvoiceInputDTO.setNgayHoaDon(formatter.format(dp_NgayHD.getDate()));
        addInvoiceInputDTO.setMaPhong(tf_MaPhong.getText());
        addInvoiceInputDTO.setDonGia(tf_DonGia.getText());
        addInvoiceInputDTO.setLoaiHoaDon(cb_LoaiHoaDon.getSelectedItem().toString());
        addInvoiceInputDTO.setSoNgay(tf_SoNgay.getText());
        addInvoiceInputDTO.setSoGio(tf_SoGio.getText());

        addInvoiceController.execute(addInvoiceInputDTO);
    }

    private void resetForm() {
        tf_TenKH.setText("");
        dp_NgayHD.setDate(Calendar.getInstance().getTime());
        tf_MaPhong.setText("");
        tf_DonGia.setText("");
        cb_LoaiHoaDon.setSelectedIndex(0);
        tf_SoNgay.setText("");
        tf_SoGio.setText("");
    }

    public void showMsgResult(AddInvoiceViewModel addInvoiceViewModel) {
        JOptionPane.showMessageDialog(this,
                addInvoiceViewModel.message,
                addInvoiceViewModel.status,
                JOptionPane.INFORMATION_MESSAGE);
    }

    public void showMsgError(AddInvoiceViewModel addInvoiceViewModel) {
        JOptionPane.showMessageDialog(this,
                addInvoiceViewModel.message,
                addInvoiceViewModel.status,
                JOptionPane.ERROR_MESSAGE);
    }

    public void setAddInvoiceController(AddInvoiceController addInvoiceController) {
        this.addInvoiceController = addInvoiceController;
    }

    public void mainShow() {
        setVisible(true);
    }
}