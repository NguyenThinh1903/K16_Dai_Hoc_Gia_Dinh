package application;

public class DeleteCommand implements Command {
    private Controller controller;

    // Constructor để khởi tạo controller
    public DeleteCommand(Controller controller) {
        this.controller = controller;
    }

    // Triển khai phương thức execute từ giao diện Command
    @Override
    public void execute() {
        String currentText = controller.getLblResult().getText();
        if (currentText.length() > 0) {
            controller.getLblResult().setText(currentText.substring(0, currentText.length() - 1));
        }
    }

    // Triển khai phương thức setController từ giao diện Command
    @Override
    public void setController(Controller controller) {
        this.controller = controller;
    }
}
