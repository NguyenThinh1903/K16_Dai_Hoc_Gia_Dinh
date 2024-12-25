package application;

public class SubtractionCommand implements Command {
    private double firstOperand;
    private double secondOperand;
    private Controller controller;

    public SubtractionCommand(double firstOperand, double secondOperand) {
        this.firstOperand = firstOperand;
        this.secondOperand = secondOperand;
    }

    @Override
    public void setController(Controller controller) {
        this.controller = controller;
    }

    @Override
    public void execute() {
        OperatorCommand subOperatorCommand = new OperatorCommand("-", firstOperand, secondOperand, controller);
        subOperatorCommand.execute();
    }
}
