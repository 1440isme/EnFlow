package vn.enflow.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Table(name = "activity_logs")
public class ActivityLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "log_id")
    Long logId;

    @Column(name = "workspace_id", nullable = false)
    Long workspaceId;

    @Column(name = "project_id", nullable = false)
    Long projectId;

    @Column(name = "task_id", nullable = false)
    Long taskId;

    @Column(name = "actor_id", nullable = false)
    Long actorId;

    @Column(name = "action", nullable = false)
    String action;

    @Column(name = "target_type")
    String targetType;

    @Column(name = "target_id")
    Long targetId;

    @Column(name = "meta_json", columnDefinition = "JSON")
    String metaJson;

    @Column(name = "created_at", nullable = false)
    LocalDateTime createdAt;

    // ── Relationships với entity đã tồn tại ──────────────────────────────────

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workspace_id", referencedColumnName = "workspace_id", insertable = false, updatable = false)
    @JsonIgnore
    Workspace workspace;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", referencedColumnName = "project_id", insertable = false, updatable = false)
    @JsonIgnore
    Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id", referencedColumnName = "task_id", insertable = false, updatable = false)
    @JsonIgnore
    Task task;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "actor_id", referencedColumnName = "user_id", insertable = false, updatable = false)
    @JsonIgnore
    User actor;
}
