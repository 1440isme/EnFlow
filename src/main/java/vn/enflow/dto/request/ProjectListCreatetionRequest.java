package vn.enflow.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProjectListCreatetionRequest {
    Long projectId;
    String name;
    String description;
    Integer position;
    Boolean isPrivate = true;
    Boolean archived = false;
    LocalDateTime createdAt;

}
