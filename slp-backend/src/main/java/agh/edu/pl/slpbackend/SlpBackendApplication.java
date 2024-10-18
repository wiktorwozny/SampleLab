package agh.edu.pl.slpbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.github.cdimascio.dotenv.Dotenv;
@SpringBootApplication
public class SlpBackendApplication {

    public static void main(String[] args) {
//        loadEnvVariables();
        SpringApplication.run(SlpBackendApplication.class, args);
    }
    private static void loadEnvVariables() {
        Dotenv dotenv = Dotenv.configure().load();
        System.getenv().forEach((k, v) -> {
            if (dotenv.get(k) != null) {
                System.setProperty(k, dotenv.get(k));
            }
        });
    }
}
