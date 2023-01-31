package pl.damian.bodzioch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
public class BotApp {
    public static void main(String[] args) {
        SpringApplication.run(BotApp.class, args);
    }
}
