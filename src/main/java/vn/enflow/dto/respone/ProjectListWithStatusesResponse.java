package vn.enflow.dto.respone;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProjectListWithStatusesResponse {
    Long listProjectId;
    String name;
    String description;
    Integer position;
    Boolean isPrivate;
    Boolean archived;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    Long projectId;
    List<StatusesRespone> statuses;
}

