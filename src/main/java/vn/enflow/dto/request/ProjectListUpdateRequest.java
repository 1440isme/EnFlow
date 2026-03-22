package vn.enflow.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProjectListUpdateRequest {
    String name;
    String projectKey;
    String description;
    Integer position;
    Boolean archived;
    Boolean isPrivate;

}
