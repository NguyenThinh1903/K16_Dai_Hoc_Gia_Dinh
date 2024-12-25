package application;

public class MultiplicationCommand implements Command {
    private double firstOperand;
    private double secondOperand;
    private Controller controller;

    public MultiplicationCommand(double firstOperand, double secondOperand) {
        this.firstOperand = firstOperand;
        this.secondOperand = secondOperand;
    }

    @Override
    public void setController(Controller controller) {
        this.controller = controller;
    }

    @Override
    public void execute() {
        OperatorCommand mulOperatorCommand = new OperatorCommand("*", firstOperand, secondOperand, controller);
        mulOperatorCommand.execute();
    }
}
