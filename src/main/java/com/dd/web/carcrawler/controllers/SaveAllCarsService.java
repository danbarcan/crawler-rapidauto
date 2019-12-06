package com.dd.web.carcrawler.controllers;

import org.apache.commons.text.StringEscapeUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.Select;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Controller
public class SaveAllCarsService {

    private String testUrl;
    private WebDriver driver;
    private final String INSERT_QUERY = "insert into table %s (%s) values (%s);\n";

    public void prepare() {
        System.setProperty(
                "webdriver.chrome.driver",
                "webdriver/chromedriver.exe");

        testUrl = "https://www.rapidauto.ro/ro//";

        ChromeOptions options = new ChromeOptions();
        //options.addArguments("headless");

        driver = new ChromeDriver(options);
        driver.manage().window().maximize();

        driver.get(testUrl);
    }

    public boolean test() {
        StringBuilder sb = new StringBuilder();
        WebElement selectManufacturerElement = driver.findElement(By.name("vehicleManufacturers"));
        AtomicInteger manufacturers = new AtomicInteger(0);
        AtomicInteger models = new AtomicInteger(0);
        AtomicInteger powers = new AtomicInteger(0);
        selectManufacturerElement.findElements(By.tagName("optgroup")).forEach(optGroup -> {

            optGroup.findElements(By.tagName("option")).forEach(manufacturerOption -> {
                System.out.println(manufacturerOption.getText());
                sb.append(String.format(INSERT_QUERY, "CAR_MANUFACTURERS", "ID, DESCRIPTION, TOP", manufacturers.incrementAndGet() + ", '" + StringEscapeUtils.escapeJava(manufacturerOption.getText()) + "', " + ("top".equalsIgnoreCase(optGroup.getAttribute("data-id")) ? 1 : 0)));
                Select selectManufacturer = new Select(selectManufacturerElement);
                selectManufacturer.selectByVisibleText(manufacturerOption.getText());
                WebElement selectModelElement = driver.findElement(By.name("vehicleModells"));
                while (!selectModelElement.isEnabled()) ;

                List<String> modelStrings = new ArrayList<>();
                boolean readModels = false;

                while (!readModels) {
                    try {
                        modelStrings = selectModelElement.findElements(By.tagName("option"))
                                .stream()
                                .filter(modelOption -> !modelOption.getText().contains("Alege"))
                                .map(WebElement::getText)
                                .collect(Collectors.toList());
                        readModels = true;
                    } catch (Exception e) {
                        System.err.println("READING MODELS ERR");
                    }
                }

                modelStrings.forEach(modelOption -> {
                    System.out.println("\t" + modelOption);
                    sb.append(String.format(INSERT_QUERY, "CAR_MODELS", "ID, DESCRIPTION, MANUFACTURER_ID", models.incrementAndGet() + ", '" + StringEscapeUtils.escapeJava(modelOption) + "', " + manufacturers.get()));
                    Select selectModel = new Select(selectModelElement);
                    selectModel.selectByVisibleText(modelOption);

                    WebElement selectVariantElement = driver.findElement(By.name("vehicleTypes"));
                    while (!selectVariantElement.isEnabled()) ;
                    List<String> powerStrings = new ArrayList<>();
                    boolean readPowers = false;
                    while (!readPowers) {
                        try {
                            powerStrings = selectVariantElement.findElements(By.tagName("option"))
                                    .stream()
                                    .filter(powerOption -> !powerOption.getText().contains("Alege"))
                                    .map(WebElement::getText)
                                    .collect(Collectors.toList());
                            readPowers = true;

                        } catch (Exception e) {
                            System.err.println("READING POWERS ERR");
                        }
                    }
                    powerStrings.forEach((powerOption -> {
                        System.out.println("\t\t" + powerOption);
                        sb.append(String.format(INSERT_QUERY, "CAR_POWERS", "ID, DESCRIPTION, MODEL_ID", powers.incrementAndGet() + ", '" + StringEscapeUtils.escapeJava(powerOption) + "', " + models.get()));
                    }));
                });

                try {
                    Files.write(Paths.get("cars_rapid_auto.sql"), sb.toString().getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        });
        System.out.println("End");

        return true;
    }

    public void teardown() {
        driver.quit();
    }
}
