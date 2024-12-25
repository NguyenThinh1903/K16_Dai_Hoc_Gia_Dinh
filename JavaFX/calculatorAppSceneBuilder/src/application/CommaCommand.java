package application;

public class CommaCommand implements Command {
    private Controller controller;

    // Thiết lập controller thông qua constructor
    public CommaCommand(Controller controller) {
        this.controller = controller;
    }

    @Override
    public void execute() {
        String currentText = controller.getLblResult().getText();
        // Chỉ thêm dấu phẩy nếu nó chưa có trong chuỗi hiện tại
        if (!currentText.contains(".")) {
            controller.getLblResult().setText(currentText + ".");
        }
    }

    @Override
    public void setController(Controller controller) {
        this.controller = controller; // Thiết lập controller
    }
}