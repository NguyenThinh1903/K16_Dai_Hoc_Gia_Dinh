package application;

import javafx.scene.control.TextField;

public class View implements Subscriber {
    private final TextField resultField;

    public View(TextField resultField) {
        this.resultField = resultField;
    }

    @Override
    public void update(String message) {
        if (message.startsWith("Lỗi:")) {
            displayError(message);
        } else {
            displayResult(message);
        }
    }

    public void displayResult(String result) {
        resultField.setText(result);
    }

    public void displayError(String errorMessage) {
        resultField.setText(errorMessage);
    }
}