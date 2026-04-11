## Project Name

EnFlow Backend (Spring Boot)

## 📌 Introduction

Repo backend API cho EnFlow, xây dựng bằng Spring Boot, dùng **MySQL** và **JWT** cho xác thực. Backend mặc định chạy với context-path **`/enflow`** để FE gọi API theo prefix `/enflow/*`.

## 🚀 Features

- REST API cho các nghiệp vụ workspace/project/task (tùy theo module hiện có trong source)
- Xác thực JWT (HS256)
- CORS cấu hình theo environment để hỗ trợ deploy FE/BE tách domain
- Profile `prod` cho cấu hình log/hiệu năng khi deploy

## 🏗️ Architecture / Tech Stack

- **Runtime**: Java 21
- **Framework**: Spring Boot (theo `pom.xml`)
- **Build tool**: Maven
- **DB**: MySQL (runtime), H2 (test)
- **Security**: Spring Security + JWT (`jjwt`)
- **Packaging**: `jar`

## 📂 Project Structure

- `src/main/java/`: mã nguồn backend
- `src/main/resources/`:
  - `application.yaml`: cấu hình chính (đọc từ biến môi trường)
  - `application-prod.yaml`: cấu hình khi chạy profile `prod`
- `.env.example`: mẫu biến môi trường 

## ⚙️ Installation & Setup

### Yêu cầu hệ thống

- Java: **21**
- Maven: 3.9+
- MySQL: 8.x (khuyến nghị)

### Chuẩn bị database

Tạo database rỗng (mặc định `EnFlow`):

```sql
CREATE DATABASE EnFlow CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### Cấu hình môi trường

Backend đọc cấu hình từ **biến môi trường**. Bạn có thể:

- Tạo file `.env` tại root (không commit) rồi export biến khi chạy, hoặc
- Set biến trực tiếp trên server / Docker / systemd.

Tham khảo `.env.example`. Các biến chính:

- **Server**
  - `SERVER_PORT` (mặc định `8080`)
  - `SERVER_SERVLET_CONTEXT_PATH` (mặc định `/enflow`)
- **Database**
  - `SPRING_DATASOURCE_URL` (mặc định `jdbc:mysql://localhost:3306/EnFlow`)
  - `SPRING_DATASOURCE_USERNAME` (mặc định `root`)
  - `SPRING_DATASOURCE_PASSWORD` (mặc định `root`)
- **JWT**
  - `ENFLOW_JWT_SECRET` (**bắt buộc**) — HS256, chuỗi UTF-8 **tối thiểu 32 ký tự**
  - `ENFLOW_JWT_EXPIRATION_MS` (mặc định `86400000`)
- **CORS**
  - `ENFLOW_CORS_ALLOWED_ORIGINS` (mặc định `http://localhost:3000`)
    - Nhiều origin: phân tách bằng dấu phẩy, ví dụ: `https://app.example.com,https://www.example.com`
- **JPA**
  - `SPRING_JPA_HIBERNATE_DDL_AUTO` (mặc định `update`)
  - `SPRING_JPA_SHOW_SQL` (mặc định `false`)

> Khuyến nghị deploy production: cân nhắc chuyển `ddl-auto` sang `validate` và dùng migration (Flyway/Liquibase) nếu cần kiểm soát schema chặt chẽ.

## ▶️ Usage

### Chạy local (dev)

```bash
mvn spring-boot:run
```

Mặc định base URL:

- `http://localhost:8080/enflow`

### Build & chạy artifact

Build:

```bash
mvn clean package
```

Chạy:

```bash
java -jar target/EnFlow-0.0.1-SNAPSHOT.war
```

### Chạy production profile

Kích hoạt profile `prod`:

Windows:

```bash
set SPRING_PROFILES_ACTIVE=prod
```

Linux:

```bash
export SPRING_PROFILES_ACTIVE=prod
```

## 🔗 API / Integration

### Tích hợp với FE

FE gọi API theo prefix `/enflow/*`. Có 2 mô hình deploy phổ biến:

- **Same-origin (khuyến nghị)**: reverse proxy route `/enflow/` → backend, FE gọi `/enflow/...` không cần CORS.
- **Tách domain (cross-origin)**:
  - FE set `NEXT_PUBLIC_API_BASE_URL=https://api.example.com`
  - BE set `ENFLOW_CORS_ALLOWED_ORIGINS=https://app.example.com`

### Context path

Nếu bạn đổi `SERVER_SERVLET_CONTEXT_PATH`, hãy đảm bảo FE (hoặc reverse proxy) vẫn route đúng prefix API tương ứng.

## 🧪 Testing

Chạy test (nếu đã cấu hình test suite):

```bash
mvn test
```




