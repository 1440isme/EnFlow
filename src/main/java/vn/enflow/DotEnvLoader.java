package vn.enflow;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Đọc file {@code .env} (KEY=value, bỏ qua dòng trống / #) và đưa vào {@link System#setProperty}
 * nếu biến môi trường OS chưa có. Không dùng thư viện ngoài — tránh lỗi classpath với DevTools / Run của IDE.
 */
final class DotEnvLoader {

    private DotEnvLoader() {}

    static void load() {
        Path envFile = findEnvFile();
        if (envFile == null) {
            return;
        }
        try {
            for (String raw : Files.readAllLines(envFile, StandardCharsets.UTF_8)) {
                String line = raw.trim();
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }
                int eq = line.indexOf('=');
                if (eq <= 0) {
                    continue;
                }
                String key = line.substring(0, eq).trim();
                if (key.isEmpty()) {
                    continue;
                }
                String value = line.substring(eq + 1).trim();
                if ((value.startsWith("\"") && value.endsWith("\""))
                        || (value.startsWith("'") && value.endsWith("'"))) {
                    value = value.substring(1, value.length() - 1);
                }
                if (System.getenv(key) == null) {
                    System.setProperty(key, value);
                }
            }
        } catch (IOException ignored) {
            // ignoreIfMissing-style: không chặn khởi động
        }
    }

    /** Đi lên từ {@code user.dir} tối đa vài cấp (khi IDE chạy từ {@code target/classes}). */
    private static Path findEnvFile() {
        List<Path> candidates = new ArrayList<>();
        Path dir = Path.of(System.getProperty("user.dir", ".")).toAbsolutePath().normalize();
        for (int i = 0; i < 10 && dir != null; i++) {
            candidates.add(dir.resolve(".env"));
            dir = dir.getParent();
        }
        for (Path p : candidates) {
            if (Files.isRegularFile(p)) {
                return p;
            }
        }
        return null;
    }
}
