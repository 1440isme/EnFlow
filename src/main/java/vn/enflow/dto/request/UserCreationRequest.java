package vn.enflow.dto.request;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class UserCreationRequest {
    private String username;

    /** Mật khẩu dạng plain text; client có thể gửi thêm field cũ `passwordHash` (alias). */
    @JsonAlias("passwordHash")
    private String password;
    private String email;
    private String fullName;
    private String avatarUrl;
    private Boolean isActive = true;
    private LocalDateTime createdAt = LocalDateTime.now();
}
