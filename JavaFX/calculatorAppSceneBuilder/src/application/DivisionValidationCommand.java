package application;

public class DivisionValidationCommand implements Command {
    private double firstOperand;
    private double secondOperand;
    private Controller controller;

    public DivisionValidationCommand(double firstOperand, double secondOperand) {
        this.firstOperand = firstOperand;
        this.secondOperand = secondOperand;
    }

    @Override
    public void setController(Controller controller) {
        this.controller = controller;
    }

    @Override
    public void execute() {
        OperatorCommand divOperatorCommand = new OperatorCommand("/", firstOperand, secondOperand, controller);
        divOperatorCommand.execute();
    }
}
