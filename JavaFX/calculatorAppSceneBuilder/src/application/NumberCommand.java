package application;

public class NumberCommand implements Command {
    private final String input; // Dữ liệu đầu vào (số hoặc dấu chấm)
    private Controller controller; // Tham chiếu đến controller

    // Constructor
    public NumberCommand(String input) {
        if (input == null || input.isEmpty()) {
            throw new IllegalArgumentException("Input không thể là null hoặc chuỗi rỗng");
        }
        this.input = input;
    }

    // Thực thi lệnh: cập nhật giá trị hiển thị trên giao diện
    @Override
    public void execute() {
        if (controller == null) {
            System.err.println("Controller chưa được thiết lập cho NumberCommand");
            return;
        }

        String currentText = controller.getLblResult().getText(); // Lấy giá trị hiện tại

        // Nếu đầu vào là dấu chấm và kết quả hiện tại chưa có dấu chấm
        if (input.equals(".") && !currentText.contains(".")) {
            controller.getLblResult().setText(currentText + ".");
        } else if (!input.equals(".")) { // Nếu là số hoặc "00"
            // Kiểm tra xem có đang bắt đầu nhập một giá trị mới không
            if (controller.isStartNewInput()) {
                controller.getLblResult().setText(input);
                controller.setStartNewInput(false); // Đánh dấu rằng đã bắt đầu nhập
            } else {
                // Nối thêm số vào chuỗi hiện tại
                controller.getLblResult().setText(currentText + input);
            }
        } else {
            // Đầu vào không hợp lệ (nếu cần xử lý thêm)
            System.err.println("Đầu vào không hợp lệ: " + input);
        }
    }

    // Thiết lập controller cho NumberCommand
    @Override
    public void setController(Controller controller) {
        if (controller == null) {
            throw new IllegalArgumentException("Controller không thể là null");
        }
        this.controller = controller;
    }
}
