package com.cnstl.wend_mach.jxl4;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import javafx.stage.Modality;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import javafx.scene.Parent;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class EvHandlerControllerRegister {

    // Регистрационный контейнер
    public class Node {
        String id = "";
        String pass = "";
        public ArrayList<String> messages;
    }

    private EvHandlerControllerMain mainController;

    public EvHandlerControllerRegister(EvHandlerControllerMain mainController) {
        this.mainController = mainController;
    }

    @FXML
    private TextField id_text_field, password_text_field;

    @FXML
    private Button register_button;

    @FXML
    private Label err_label;

    public Node data = new Node();

    @FXML
    private void colorResetID() {
        id_text_field.setStyle("-fx-control-inner-background: white;");
    }

    @FXML
    private void colorResetPass() {
        password_text_field.setStyle("-fx-control-inner-background: white;");
    }

    @FXML
    private void handleButtonAction() throws IOException {
        String id = id_text_field.getText();
        String password = password_text_field.getText();

        if (id.isEmpty() || password.isEmpty()) {
            err_label.setText("Заполните все поля!");
            return;
        }

        data.id = id;
        data.pass = password;
        data.messages = new ArrayList<>();

        Stage stage = (Stage) register_button.getScene().getWindow();
        stage.close();
        openChatWindow();
    }

    private void closeCurrentWindow() {
        Stage stage = (Stage) register_button.getScene().getWindow();
        stage.close();
    }

    private int loadUserDataFromJson(String userId) { //todo: защита от переоткрытия
        String fileName = userId + ".json";
        File file = new File(fileName);
        if (!file.exists()) {
            return 1;
        }

        try (Reader reader = new FileReader(fileName)) {
            Gson gson = new Gson();
            data = gson.fromJson(reader, Node.class);
        } catch (IOException e) {
            // Обработка ошибки чтения файла
            e.printStackTrace();
        }
        return 0;
    }


    @FXML
    private void login() {
        String id = id_text_field.getText();
        String password = password_text_field.getText();

        if (id.isEmpty() || password.isEmpty()) {
            if (id.isEmpty()) {
                id_text_field.setStyle("-fx-background-color: #FF7272;");
            }
            if (password.isEmpty()) {
                password_text_field.setStyle("-fx-background-color: #FF7272;");
            }
            err_label.setText("Заполните поля!");
            return;
        }

        int code = loadUserDataFromJson(id);
        if (!id.isEmpty() && code != 1) {
            if (!password.isEmpty() && data.pass.equals(password_text_field.getText()))
            {
                closeCurrentWindow();
                openChatWindow();
            } else {
                err_label.setText("Неверный пароль");
                password_text_field.setStyle("-fx-background-color: #FF7272;");
            }
        } else {
            err_label.setText("Пользователь не найден");
            id_text_field.setStyle("-fx-background-color: #FF7272;");
        }
    }

    private void openChatWindow() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("chat_interface.fxml"));
            Parent root1 = fxmlLoader.load();

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("");
            stage.setScene(new Scene(root1));
            stage.show();

            EvHandlerControllerMain chat = (EvHandlerControllerMain) fxmlLoader.getController();
            chat.setCurrNode(data);
            chat.ready();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public EvHandlerControllerRegister() {}

    public void saveToJson() {
        Gson gson = new GsonBuilder().setLenient().create();
        try (FileWriter writer = new FileWriter(data.id + ".json")) {
            gson.toJson(data, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
