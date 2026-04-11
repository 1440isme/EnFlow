package vn.enflow.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProjectCreatetionRequest {
    Long workspaceId;
    String name;
    String projectKey;
    String description;
    Boolean isPrivate = false;
    Boolean archive = false;
    LocalDateTime createdAt;
}
