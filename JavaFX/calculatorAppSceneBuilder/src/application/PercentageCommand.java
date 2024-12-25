package application;

public class PercentageCommand implements Command {
    private Controller controller;

    @Override
    public void execute() {
        try {
            String currentText = controller.getLblResult().getText();
            if (!currentText.isEmpty()) {
                double value = Double.parseDouble(currentText);
                double result = value / 100; // Chuyển đổi số thành phần trăm
                controller.updateCalculationResult(result);
            }
        } catch (NumberFormatException e) {
            controller.displayError("Giá trị không hợp lệ để tính phần trăm");
        }
    }

    @Override
    public void setController(Controller controller) {
        this.controller = controller;
    }
}