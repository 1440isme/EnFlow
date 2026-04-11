package vn.enflow.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    Long notificationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    User user;

    @Column(name = "workspace_id")
    Long workspaceId;

    /** Ví dụ: TASK_ASSIGNED */
    @Column(name = "type", nullable = false, length = 64)
    String type;

    @Column(name = "title", nullable = false, length = 255)
    String title;

    @Column(name = "body", columnDefinition = "TEXT")
    String body;

    @Column(name = "task_id")
    Long taskId;

    @Column(name = "project_id")
    Long projectId;

    @Column(name = "read_at")
    LocalDateTime readAt;

    @Column(name = "created_at", nullable = false)
    LocalDateTime createdAt;
}
