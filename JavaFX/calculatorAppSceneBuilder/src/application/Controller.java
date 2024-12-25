package application;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Controller {

    @FXML
    private Pane titlePane;
    @FXML
    private Label lblResult;
    @FXML
    private ImageView btnClose, btnMinimize;
    @FXML
    private Pane btnPlus, btnMinus, btnMultiply, btnDivide, btnEquals, 
                 btnClear, btnPercentage, btnDelete, btnComma;
    @FXML
    private Pane btn0, btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9, btn00;

    private double x, y;
    private Model model;
    private boolean startNewInput = true;
    private MacroCommand macroCommand;
    private double firstOperand;
    private String operator;
    private StringBuilder currentExpression = new StringBuilder();

    public void init(Stage stage) {
        try {
            model = new Model();
            macroCommand = new MacroCommand(this);

            // Khởi tạo các command
            ClearCommand clearCommand = new ClearCommand();
            clearCommand.setController(this);
            macroCommand.addCommand("clear", clearCommand);

            PercentageCommand percentageCommand = new PercentageCommand();
            percentageCommand.setController(this);
            macroCommand.addCommand("percentage", percentageCommand);

            setupEventHandlers(stage);
            lblResult.setText("");
        } catch (Exception e) {
            displayError("Lỗi khởi tạo: " + e.getMessage());
        }
    }

    private void setupEventHandlers(Stage stage) {
        setupWindowDragEvent(stage);
        setupCloseAndMinimizeEvents(stage);
    }

    private void setupWindowDragEvent(Stage stage) {
        titlePane.setOnMousePressed(mouseEvent -> {
            x = mouseEvent.getSceneX();
            y = mouseEvent.getSceneY();
        });

        titlePane.setOnMouseDragged(mouseEvent -> {
            stage.setX(mouseEvent.getScreenX() - x);
            stage.setY(mouseEvent.getScreenY() - y);
        });
    }

    private void setupCloseAndMinimizeEvents(Stage stage) {
        btnClose.setOnMouseClicked(event -> stage.close());
        btnMinimize.setOnMouseClicked(event -> stage.setIconified(true));
    }

    @FXML
    private void onNumberClicked(MouseEvent event) {
        Pane source = (Pane) event.getSource();
        String number = source.getId().replace("btn", "");
        if (number.equals("Comma")) {
            addCommaToResult();
        } else {
            onNumberInput(number);
        }
    }

    @FXML
    private void onSymbolClicked(MouseEvent event) {
        Pane source = (Pane) event.getSource();
        String symbol = source.getId().replace("btn", "");
        onSymbolInput(symbol);
    }

    private void onNumberInput(String value) {
        if (startNewInput) {
            currentExpression = new StringBuilder(value);
            startNewInput = false;
        } else {
            currentExpression.append(value);
        }
        updateDisplay();
    }

    private void onSymbolInput(String symbol) {
        switch (symbol) {
            case "Equals":
                performCalculation();
                break;
            case "Clear":
                macroCommand.execute("clear");
                startNewInput = true;
                currentExpression = new StringBuilder();
                updateDisplay();
                break;
            case "Plus":
                setOperator("+");
                break;
            case "Minus":
                setOperator("-");
                break;
            case "Multiply":
                setOperator("*");
                break;
            case "Divide":
                setOperator("/");
                break;
            case "Delete":
                onDeleteClicked();
                break;
            case "Percentage":
                handlePercentage();
                break;
            default:
                displayError("Phép toán không hợp lệ");
        }
    }

    private void handlePercentage() {
        if (!currentExpression.toString().isEmpty()) {
            try {
                if (operator != null && currentExpression.toString().split(" ").length == 3) {
                    // Nếu đang trong phép tính, áp dụng phần trăm cho số thứ hai
                    String[] parts = currentExpression.toString().split(" ");
                    double value = Double.parseDouble(parts[2]);
                    double percentValue = value / 100;
                    
                    // Cập nhật biểu thức với giá trị phần trăm
                    currentExpression = new StringBuilder(parts[0] + " " + parts[1] + " " + percentValue);
                    updateDisplay();
                } else {
                    // Nếu chỉ có một số, tính phần trăm trực tiếp
                    double value = Double.parseDouble(currentExpression.toString());
                    double result = value / 100;
                    updateCalculationResult(result);
                }
            } catch (NumberFormatException e) {
                displayError("Giá trị không hợp lệ để tính phần trăm");
            }
        }
    }

    private void addCommaToResult() {
        if (!currentExpression.toString().contains(".")) {
            currentExpression.append(".");
            updateDisplay();
        }
    }

    public void setOperator(String operator) {
        if (!currentExpression.toString().isEmpty()) {
            if (Character.isDigit(currentExpression.charAt(currentExpression.length() - 1))) {
                currentExpression.append(" ").append(operator).append(" ");
                this.operator = operator;
                this.firstOperand = Double.parseDouble(currentExpression.toString().split(" ")[0]);
                startNewInput = false;
                updateDisplay();
            }
        }
    }

    
    public void performCalculation() {
        if (operator != null && currentExpression.toString().split(" ").length == 3) {
            String[] parts = currentExpression.toString().split(" ");
            double secondOperand = Double.parseDouble(parts[2]);
            Command command = new OperatorCommand(operator, firstOperand, secondOperand, this);
            macroCommand.addCommand("calculate", command);
            macroCommand.execute("calculate");
        } else if (currentExpression.length() > 0) {
            try {
                double result = Double.parseDouble(currentExpression.toString());
                updateCalculationResult(result);
            } catch (NumberFormatException e) {
                displayError("Giá trị không hợp lệ");
            }
        } else {
            displayError("Vui lòng nhập đầy đủ phép tính");
        }
    }

    public void onDeleteClicked() {
        if (currentExpression.length() > 0) {
            currentExpression.setLength(currentExpression.length() - 1);
            updateDisplay();
        }
    }

    private void updateDisplay() {
        lblResult.setText(currentExpression.toString());
    }

    public void updateCalculationResult(double result) {
        String resultStr = result % 1 == 0 ? 
            String.valueOf((int) result) : 
            String.format("%.2f", result);
        currentExpression = new StringBuilder(resultStr);
        updateDisplay();
        startNewInput = true;
    }

    public void displayError(String message) {
        lblResult.setText("Lỗi: " + message);
        startNewInput = true;
        currentExpression = new StringBuilder();
    }

    // Getters and Setters
    public Label getLblResult() {
        return lblResult;
    }

    public void setStartNewInput(boolean startNewInput) {
        this.startNewInput = startNewInput;
    }

    public Model getModel() {
        return model;
    }

    public boolean isStartNewInput() {
        return startNewInput;
    }
}
