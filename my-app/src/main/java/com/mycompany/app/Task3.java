package com.mycompany.app;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.By;
import java.io.FileWriter;

public class Task3 {
    public static void getWeatherForecast() {
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\user\\Downloads\\chromedriver-win64\\chromedriver-win64\\chromedriver.exe");
        WebDriver driver = new ChromeDriver();

        try {
            String url = "https://api.open-meteo.com/v1/forecast?latitude=56.3287&longitude=44.002&hourly=temperature_2m,rain&timezone=Europe%2FMoscow&forecast_days=1";

            driver.get(url);
            WebElement elem = driver.findElement(By.tagName("pre"));
            String jsonStr = elem.getText();
            System.out.println("Полученные данные: " + jsonStr); // Добавим вывод сырых данных

            JSONParser parser = new JSONParser();
            JSONObject obj = (JSONObject) parser.parse(jsonStr);

            if (!obj.containsKey("hourly")) {
                throw new Exception("API не вернул данные о погоде. Проверьте параметры запроса.");
            }

            JSONObject hourly = (JSONObject) obj.get("hourly");
            JSONArray times = (JSONArray) hourly.get("time");
            JSONArray temps = (JSONArray) hourly.get("temperature_2m");
            JSONArray rains = (JSONArray) hourly.get("rain");

            // Создаем папку result, если ее нет
            new java.io.File("result").mkdirs();

            // Вывод в консоль
            System.out.println("\nПрогноз погоды в Нижнем Новгороде:");
            System.out.println("№\tВремя\t\tТемпература\tОсадки");

            // Запись в файл
            try (FileWriter writer = new FileWriter("result/forecast.txt")) {
                writer.write("Прогноз погоды в Нижнем Новгороде:\n");
                writer.write("№\tВремя\t\tТемпература\tОсадки\n");

                for (int i = 0; i < times.size(); i++) {
                    String time = ((String) times.get(i)).substring(11);
                    String temp = temps.get(i).toString();
                    String rain = rains.get(i).toString();

                    System.out.printf("%d\t%s\t%s°C\t\t%s мм\n",
                            i+1, time, temp, rain);
                    writer.write(String.format("%d\t%s\t%s°C\t\t%s мм\n",
                            i+1, time, temp, rain));
                }
                System.out.println("Данные сохранены в result/forecast.txt");
            }
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        } finally {
            driver.quit();
        }
    }
}