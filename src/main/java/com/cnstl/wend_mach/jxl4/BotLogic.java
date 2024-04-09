package com.cnstl.wend_mach.jxl4;
import org.jsoup.Jsoup;
import java.net.InetAddress;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import org.json.JSONObject;
import java.util.Random;

public class BotLogic {

    private int requestId = 0;
    private boolean workWithMultifunction = false;
    private String status = "none";
    private boolean readingMode = false;
    private String[] data;


    public BotLogic() {
        //
    }
    public void setRequestId(int requestId) {
        this.requestId = requestId;
    } //неактуально

    public int getRequestId() {
        return requestId;
    } //неактуально

    private int helloCount = 0;
    private int goodbyeCount = 0;

    //Функция генерации ответа
    public String respond(String userInput) {
    //проверять что я у него спросил лучше тут
        userInput = userInput.toLowerCase();
        //перебераем регексы, которые объединяются в одно выражение, а не в кучу
        if (userInput.matches("(.*)(пр.в.т|((добр.й?)\s(день|вечер))|[зсc3]дра(в?)ст(в?)уй(т?)(е?)|с.л.м|[зс]д.р.в.|х[аеэ]й)(.*)(\n?)")) { helloCount++; return hello(userInput);}
        if (userInput.matches("(.*)((пр.щ.й((те)?))|п[оа]к.(д?)(а?)|х.р.ш.[гв].\sдн.|уд.ч.|(до?)(\s?)св.д.н(и?).)(.*)(\n?)"))  {goodbyeCount++; return isGoodbye(userInput);}
        if (userInput.matches("(.*)(вр.м.)(.*)(\n?)")) return needTime(userInput);
        if (userInput.matches("(.*)(пинг|ping)(.*)(\n?)")) return needPing(userInput);
        if (userInput.matches("(.*)((случ.йн..)?(\s?)ч.сл.)(.*)(\n?)")) return String.valueOf(needNumbers(userInput));
        if (userInput.matches("(.*)(в.лют.)(.*)(\n?)")) return getValuta(userInput);
        if (userInput.matches("(.*)(help|h|[чшщ](т?)о(\s?)м.[гж][её][шщ]([ьб]?))(.*)(\n?)")) return tellAboutCapabilities();
        else return "Это интересно! Я еще учусь и не могу обсуждать всё, но давайте продолжим разговор.";
    }

    //Проверка на приветствие
    private String hello(String userInput) { //userInput убрать в случае ненадобности
        if (helloCount <= 2)
            return "Доброго времени суток! Чем могу быть полезен?";
        else return "Мы уже здоровались!";
    }

    //Проверка на прощание
    private String isGoodbye(String userInput) {
        if (goodbyeCount <= 1)
            return "Всего доброго! Обращайтесь, если будут ещё вопросы.";
        else return "Мы уже прощались!";
    }

    // Метод для рассказа о возможностях бота
    private String tellAboutCapabilities() {
        return "Я могу отвечать на приветствия, прощаться, давать текущее время, пинговать хост и генерировать случайные числа.";
    }

    //Вывод валюты
    private String getValuta(String user_input) {
        try {
            String url = "https://www.cbr-xml-daily.ru/daily_utf8.xml";
            org.jsoup.nodes.Document document = Jsoup.connect(url).get();

            org.jsoup.nodes.Element valuteElement = document.selectFirst("Valute[ID=R01375]");
            if (valuteElement != null) {
                String valuteName = valuteElement.selectFirst("Name").text();
                String valuteValue = valuteElement.selectFirst("Value").text();
                return "Valute: " + valuteName + ", Value: " + valuteValue;
            } else {
                return "Valute with ID R01375 not found";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Error retrieving valuta: " + e.getMessage();
        }
    }






    //Проверка на запрос времени
    private String needTime(String userInput) {
        try {
                // Создаем подключение к API
                URL url = new URL("http://worldtimeapi.org/api/timezone/Asia/Chita");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");

                // Получаем ответ от сервера
                int responseCode = conn.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    // Читаем ответ
                    Scanner scanner = new Scanner(conn.getInputStream());
                    StringBuilder response = new StringBuilder();
                    while (scanner.hasNextLine()) {
                        response.append(scanner.nextLine());
                    }
                    scanner.close();

                    // Парсим JSON и извлекаем информацию о времени
                    JSONObject jsonResponse = new JSONObject(response.toString());
                    String datetime = jsonResponse.getString("datetime");

                    // Возвращаем время
                    return "Текущее время: " + datetime;
                } else {
                    return "Не удалось получить время. Код ответа: " + responseCode;
                }

        } catch (Exception e) {
            e.printStackTrace();
            return "Ошибка при получении времени: " + e.getMessage();
        }
    }


    //Проверка на запрос пинга (желательно вводить сайт)
    private String needPing(String userInput) {
            try {
                // Выполняем пинг и измеряем время ответа
                long startTime = System.currentTimeMillis();
                InetAddress address = InetAddress.getByName("8.8.8.8");
                boolean reachable = address.isReachable(5000); // Проверяем доступность хоста с таймаутом 5000 мс (5 секунд)
                long endTime = System.currentTimeMillis();

                // Если хост доступен, возвращаем время ответа в миллисекундах
                if (reachable) {
                    return "Пинг: " + address.toString() + " " + (endTime - startTime) + " мс";
                } else {
                    return "Хост недоступен";
                }
            } catch (Exception e) {
                e.printStackTrace();
                return "Ошибка при выполнении пинга: " + e.getMessage();
            }
    }

    //Считает цифры
    double needNumbers(String userInput) {

            Random random = new Random();

            return random.nextDouble();
    }

}

