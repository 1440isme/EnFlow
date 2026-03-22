package vn.enflow.dto.respone;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TaskAssigneeResponse {
    Long taskId;
    Long userId;
    String username;
    String fullName;
    String email;
    Boolean isPrimary;
    LocalDateTime assignedAt;
}
