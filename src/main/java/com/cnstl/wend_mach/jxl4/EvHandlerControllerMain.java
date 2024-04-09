package com.cnstl.wend_mach.jxl4;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class EvHandlerControllerMain {

    @FXML
    Text main_text; // Текстовое поле с ответами

    @FXML
    TextArea input_area; // Текстовое поле для запросов

    @FXML
    ScrollPane mes_scroll_pane;

    private BotLogic Bt1 = new BotLogic(); // Экземпляр логики бота
    private EvHandlerControllerRegister.Node currNode;

    public void setCurrNode(EvHandlerControllerRegister.Node nd) {
        currNode = nd;
    }

    public void ready() {
        // Начальная инициализация текстового поля
        if (currNode.messages.size() == 0) {
            main_text.setText("");
            appendMessage("Бот: Приветствую! Чего желаете?\n");
        } else {
            main_text.setText(combineMessages());
        }
    }


    // Ввод запроса на кнопку ENTER
    @FXML
    private void enterText(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            String input = input_area.getText();
            appendMessage("Вы: " + input); // Сохраняем сообщение пользователя
            appendMessage("Бот: " + Bt1.respond(input) + "\n"); // Сохраняем ответ бота
            input_area.clear();
        }
        mes_scroll_pane.setVvalue(mes_scroll_pane.getVmax());
    }

    // Запоминание сообщений в список
    private void appendMessage(String message) {
        // Создаем новый узел для сообщения
        if (currNode.messages == null)
            currNode.messages = new ArrayList<>();
        currNode.messages.add(message);
        main_text.setText(main_text.getText() + "\n" + message);
    }

    // Возвращаем сообщения в тексте
    private String combineMessages() {
        StringBuilder combinedText = new StringBuilder();

        // Получаем список сообщений из активного узла
        ArrayList<String> messages = currNode.messages;
        for (String message : messages) {
            combinedText.append("").append(message).append("\n");
        }

        return combinedText.toString();
    }

}
