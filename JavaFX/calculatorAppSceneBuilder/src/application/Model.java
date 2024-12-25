package application;

public class Model extends Publisher {
    private double firstOperand;
    private double secondOperand;
    private String operator;
    private double result;

    public double getFirstOperand() {
        return firstOperand;
    }

    public void setFirstOperand(double firstOperand) {
        this.firstOperand = firstOperand;
        notifySubscribers("Operand 1 updated");  // Thông báo khi có sự thay đổi
    }

    public double getSecondOperand() {
        return secondOperand;
    }

    public void setSecondOperand(double secondOperand) {
        this.secondOperand = secondOperand;
        notifySubscribers("Operand 2 updated");  // Thông báo khi có sự thay đổi
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
        notifySubscribers("Operator updated");  // Thông báo khi có sự thay đổi
    }

    public double getResult() {
        return result;
    }

    public void setResult(double result) {
        this.result = result;
        notifySubscribers("Result updated");  // Thông báo khi có sự thay đổi
    }
}
