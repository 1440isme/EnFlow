package vn.enflow.dto.respone;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TaskTagResponse {
    Long taskId;
    Long tagId;
    String tagName;
    String tagColor;
}
