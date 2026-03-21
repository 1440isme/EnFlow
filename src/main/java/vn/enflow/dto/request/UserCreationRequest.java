package vn.enflow.dto.request;

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
    private String passwordHash;
    private String email;
    private String fullName;
    private String avatarUrl;
    private Boolean isActive = true;
    private LocalDateTime createdAt = LocalDateTime.now();
}
