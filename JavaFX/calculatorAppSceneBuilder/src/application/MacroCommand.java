package application;

import java.util.HashMap;
import java.util.Map;

public class MacroCommand implements Command {
    private final Map<String, Command> commands = new HashMap<>();
    private Controller controller;

    // Constructor
    public MacroCommand(Controller controller) {
        this.controller = controller;
    }

    // Thêm lệnh vào MacroCommand
    public void addCommand(String key, Command command) {
        if (command != null) {
            command.setController(controller);  // Đảm bảo lệnh có controller
            commands.put(key, command);
        } else {
            controller.displayError("Lệnh không hợp lệ.");
        }
    }

    // Thiết lập lại controller cho tất cả các lệnh
    @Override
    public void setController(Controller controller) {
        this.controller = controller;
        // Cập nhật controller cho tất cả các lệnh
        commands.values().forEach(command -> command.setController(controller));
    }

    // Phương thức execute không làm gì cả ở đây
    @Override
    public void execute() {
        // Để trống, vì MacroCommand không thực thi trực tiếp mà qua execute với key
    }

    // Thực thi lệnh theo key
    public void execute(String key) {
        Command command = commands.get(key);  // Tìm lệnh theo key
        if (command != null) {
            command.execute();  // Thực thi lệnh
        } else {
            // Nếu không tìm thấy lệnh, hiển thị lỗi
            controller.displayError("Chức năng không tồn tại: " + key);
        }
    }

  
}
