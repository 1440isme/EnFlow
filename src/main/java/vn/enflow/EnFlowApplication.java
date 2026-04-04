package vn.enflow;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EnFlowApplication {

    public static void main(String[] args) {
        loadDotEnv();
        SpringApplication.run(EnFlowApplication.class, args);
    }

    /** Nạp `.env` từ thư mục làm việc hiện tại; không ghi đè biến môi trường OS đã có (prod/Docker). */
    private static void loadDotEnv() {
        Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
        dotenv
                .entries()
                .forEach(e -> {
                    String key = e.getKey();
                    if (System.getenv(key) == null) {
                        System.setProperty(key, e.getValue());
                    }
                });
    }
}
