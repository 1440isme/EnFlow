package vn.enflow.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "workspaces")
public class Workspace {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "workspace_id")
    Long workspaceId;

    @Column(name = "name", nullable = false)
    String name;

    @Column(name = "workspace_key", unique = true)
    String workspaceKey;

    @Column(name = "description", columnDefinition = "TEXT")
    String description;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_user_id", nullable = false)
    User owner;

    @Column(name = "is_private", nullable = false)
    Boolean isPrivate = false;

    @Column(name = "created_at", nullable = false)
    LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    LocalDateTime updatedAt;

    // ── Relationships với entity đã tồn tại ──────────────────────────────────

    @JsonIgnore
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "workspace", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<WorkspaceMember> members;

    // ── Relationships với entity chưa tồn tại (mở comment khi tạo entity) ───

    // projects.workspace_id → workspaces.workspace_id
    @JsonIgnore
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "workspace", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<Project> projects;

    // tags.workspace_id → workspaces.workspace_id
    @JsonIgnore
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "workspace", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<Tag> tags;

    // activity_logs.workspace_id → workspaces.workspace_id
    // @JsonIgnore
    // @ToString.Exclude
    // @EqualsAndHashCode.Exclude
    // @OneToMany(mappedBy = "workspace", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    // List<ActivityLog> activityLogs;
}
