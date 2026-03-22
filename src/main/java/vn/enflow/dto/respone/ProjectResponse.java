package vn.enflow.dto.respone;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProjectResponse {
    Long idProject;
    String name;
    String projectKey;
    String description;
    Boolean isPrivate;
    Boolean archived;
    Long workspaceId;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
