package com.cnstl.wend_mach.jxl4;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Класс приложения
 */
public class U2Ccontroller extends Application {
    private EvHandlerControllerRegister controller; // костыль для сохранения данных

    @Override
    // Выполняется при запуске
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(U2Ccontroller.class.getResource("hello-view.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root, 320, 400);
        stage.setScene(scene);

        controller = loader.getController(); // Получаем контроллер после загрузки FXML
        stage.setTitle("");
        stage.show();
    }

    @Override
    // Выполняется при закрытии
    public void stop() throws Exception {
        if (controller != null) {
            controller.saveToJson();
        }
        super.stop();
    }

    // Запускает приложение
    public static void main(String[] args) {
        launch();
    }
}
