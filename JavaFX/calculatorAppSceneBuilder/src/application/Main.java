package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // Tải tệp FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("MainWindowInterface.fxml"));
            Parent root = loader.load();

            // Tạo và thiết lập Scene
            Scene scene = new Scene(root);
            scene.setFill(Color.TRANSPARENT);  // Nền trong suốt
            primaryStage.setScene(scene);

            // Thiết lập cửa sổ chính (primary stage)
            primaryStage.initStyle(StageStyle.TRANSPARENT);  // Kiểu cửa sổ trong suốt
            primaryStage.setResizable(false);  // Không thay đổi kích thước cửa sổ
            primaryStage.setTitle("Calculator");

            // Thêm các icon vào cửa sổ chính
            primaryStage.getIcons().addAll(
                new Image(getClass().getResourceAsStream("close.png")),
                new Image(getClass().getResourceAsStream("hide.png")),
                new Image(getClass().getResourceAsStream("icon.png")),
                new Image(getClass().getResourceAsStream("delete.png"))
            );

            // Lấy controller từ loader và thiết lập thêm dữ liệu nếu cần
            Controller controller = loader.getController();
            controller.init(primaryStage);

            // Hiển thị cửa sổ
            primaryStage.show();
        } catch (Exception e) {
            // In ra lỗi nếu gặp phải vấn đề khi tải FXML
            e.printStackTrace();
            System.err.println("ERROR: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
