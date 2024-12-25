package application;

public class AddCommand implements Command {
    private double firstOperand;
    private double secondOperand;
    private Controller controller;

    public AddCommand(double firstOperand, double secondOperand) {
        this.firstOperand = firstOperand;
        this.secondOperand = secondOperand;
    }

    @Override
    public void setController(Controller controller) {
        this.controller = controller;
    }

    @Override
    public void execute() {
        OperatorCommand addOperatorCommand = new OperatorCommand("+", firstOperand, secondOperand, controller);
        addOperatorCommand.execute();
    }
}
