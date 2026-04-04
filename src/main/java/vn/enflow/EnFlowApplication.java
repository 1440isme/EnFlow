package vn.enflow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EnFlowApplication {

    public static void main(String[] args) {
        DotEnvLoader.load();
        SpringApplication.run(EnFlowApplication.class, args);
    }
}
