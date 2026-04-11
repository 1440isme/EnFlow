package vn.enflow.dto.respone;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StatusesRespone {
    Long statusId;
    String statusGroup;
    String color;
    Integer position;
    Boolean isDefault;
    Long listId;
    Long projectId;
}
