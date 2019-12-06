package com.dd.web.carcrawler;

import com.dd.web.carcrawler.controllers.SaveAllCarsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CarCrawlerApplication {
    private static SaveAllCarsService saveAllCarsService;

    @Autowired
    public CarCrawlerApplication(SaveAllCarsService saveAllCarsService) {
        this.saveAllCarsService = saveAllCarsService;
    }


    public static void main(String[] args) {
        SpringApplication.run(CarCrawlerApplication.class, args);

        boolean finished = false;

        while (!finished) {

            try {
                saveAllCarsService.prepare();
                finished = saveAllCarsService.test();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                saveAllCarsService.teardown();
            }
        }

    }

}
