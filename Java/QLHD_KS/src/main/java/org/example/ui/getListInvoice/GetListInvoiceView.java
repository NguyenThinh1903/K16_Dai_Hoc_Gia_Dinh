package org.example.ui.getListInvoice;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class GetListInvoiceView extends JFrame {
    private GetListInvoiceController getListInvoiceController;
    private List<GetListInvoiceViewModel> listInvoice; // Không khởi tạo ở đây

    public GetListInvoiceView() {

    }

    public void mainShow() {
        getContentPane().removeAll(); // Xóa nội dung cũ
        getListInvoiceController.execute(); // Gọi controller để lấy dữ liệu
    }

    public void showResult(List<GetListInvoiceViewModel> listInvoice) {
        this.listInvoice = listInvoice; // Cập nhật danh sách hóa đơn
        setTitle("Xuất hóa đơn khách sạn");
        setResizable(false);
        setSize(900, 500);

        // Tạo tiêu đề
        JLabel titleLabel = new JLabel("DANH SÁCH HÓA ĐƠN KHÁCH SẠN", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0)); // Padding xung quanh tiêu đề

        // Cột tiêu đề cho JTable
        String[] columns = {
                "Mã HD", "Tên KH", "Ngày HD", "Mã phòng", "Đơn giá", "Loại HD", "Số ngày", "Số giờ", "Thành tiền"
        };

        // Tạo model cho bảng
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0);
        JTable table = new JTable(tableModel);

        // Custom renderer
        table.setDefaultRenderer(Object.class, new InvoiceCellRenderer());

        // Thêm dữ liệu hóa đơn vào bảng
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
                    invoice.thanhTien
            };
            tableModel.addRow(row);
        }

        // Thêm bảng vào JScrollPane
        JScrollPane scrollPane = new JScrollPane(table);

        // Thiết lập layout cho frame
        this.setLayout(new BorderLayout());
        this.add(titleLabel, BorderLayout.NORTH); // Thêm tiêu đề ở trên cùng
        this.add(scrollPane, BorderLayout.CENTER); // Thêm bảng ở giữa

        // Đặt vị trí frame ở giữa màn hình
        this.setLocationRelativeTo(null);
        this.setVisible(true); // Hiển thị frame
    }

    class InvoiceCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            GetListInvoiceViewModel vm = listInvoice.get(row); // Lấy thông tin hóa đơn
            return c;
        }
    }

    public void setGetListInvoiceController(GetListInvoiceController getListInvoiceController) {
        this.getListInvoiceController = getListInvoiceController;
    }

    public void showMsgError(GetListInvoiceViewModel responseData) {
        // Hiển thị thông báo lỗi
        JOptionPane.showMessageDialog(null,
                responseData.msg,
                responseData.status,
                JOptionPane.ERROR_MESSAGE);
    }
}