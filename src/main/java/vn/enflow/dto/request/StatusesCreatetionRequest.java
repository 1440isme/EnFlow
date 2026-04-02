package vn.enflow.dto.request;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StatusesCreatetionRequest {
    Long idProject;
    Long idListProject;
    String color;
    String statusGroup;
    String position;
    Boolean isDefault = true;
}
