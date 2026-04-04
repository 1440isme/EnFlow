package vn.enflow.dto.respone;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NotificationResponse {
    Long notificationId;
    Long workspaceId;
    String type;
    String title;
    String body;
    Long taskId;
    Long projectId;
    LocalDateTime readAt;
    LocalDateTime createdAt;
}
