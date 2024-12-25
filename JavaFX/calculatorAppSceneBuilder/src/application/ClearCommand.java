package application;

public class ClearCommand implements Command {
    private Controller controller;

    @Override
    public void execute() {
        if (controller != null) {
            // Đặt lblResult thành chuỗi trống để xóa kết quả
            controller.getLblResult().setText(""); 
            
            // Reset lại các giá trị trong Model (firstOperand, secondOperand, operator)
            controller.getModel().setFirstOperand(0);
            controller.getModel().setSecondOperand(0);
            controller.getModel().setOperator(null);
            controller.getModel().setResult(0);
            
            // Đánh dấu cho phép bắt đầu nhập mới
            controller.setStartNewInput(true); 
        } else {
            System.out.println("Controller chưa được thiết lập cho ClearCommand");
        }
    }

    @Override
    public void setController(Controller controller) {
        this.controller = controller; // Thiết lập controller
    }
}
