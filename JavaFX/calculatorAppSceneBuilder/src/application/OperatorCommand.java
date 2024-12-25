package application;

public class OperatorCommand implements Command {
    private final double firstOperand;
    private final double secondOperand;
    private final String operator;
    private Controller controller;

    public OperatorCommand(String operator, double firstOperand, double secondOperand, Controller controller) {
        if (operator == null || operator.isEmpty()) {
            throw new IllegalArgumentException("Operator cannot be null or empty");
        }
        if (controller == null) {
            throw new IllegalArgumentException("Controller cannot be null");
        }

        this.operator = operator;
        this.firstOperand = firstOperand;
        this.secondOperand = secondOperand;
        this.controller = controller;
    }

    @Override
    public void execute() {
        try {
            validateInput();
            double result = calculateResult();
            controller.updateCalculationResult(result);
        } catch (ArithmeticException e) {
            controller.displayError("Không thể chia cho 0");
        } catch (IllegalArgumentException e) {
            controller.displayError("Toán tử không hợp lệ: " + operator);
        } catch (Exception e) {
            controller.displayError("Lỗi trong quá trình tính toán");
        }
    }

    private void validateInput() {
        if (operator.equals("/") && secondOperand == 0) {
            throw new ArithmeticException("Không thể chia cho 0");
        }
    }

    private double calculateResult() {
        switch (operator) {
            case "+":
                return firstOperand + secondOperand;
            case "-":
                return firstOperand - secondOperand;
            case "*":
                return firstOperand * secondOperand;
            case "/":
                return firstOperand / secondOperand;
            case "%":
                return firstOperand * (secondOperand / 100); // Tính phần trăm của số
            default:
                throw new IllegalArgumentException("Toán tử không hợp lệ: " + operator);
        }
    }
    @Override
    public void setController(Controller controller) {
        if (controller == null) {
            throw new IllegalArgumentException("Controller cannot be null");
        }
        this.controller = controller;
    }
}