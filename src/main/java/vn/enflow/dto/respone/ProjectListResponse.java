package vn.enflow.dto.respone;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProjectListResponse {
    Long listProjectId;
    String name;
    String description;
    Integer position;
    Boolean isPrivate;
    Boolean archived;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    Long projectId;
}
