package vn.enflow.dto.request;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProjectUpdateRequest {
    String name;
    String project_key;
    String description;
    Boolean is_private;
    Boolean archived;

}
