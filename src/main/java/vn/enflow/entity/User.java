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
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    Long userId;

    @Column(name = "email", nullable = false, unique = true)
    String email;

    @Column(name = "username", unique = true)
    String username;

    @Column(name = "password_hash", nullable = false)
    String passwordHash;

    @Column(name = "full_name", nullable = false)
    String fullName;

    @Column(name = "avatar_url")
    String avatarUrl;

    @Column(name = "is_active", nullable = false)
    Boolean isActive = true;

    @Column(name = "created_at", nullable = false)
    LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    LocalDateTime updatedAt;

    // ── Relationships với entity đã tồn tại ──────────────────────────────────

    @JsonIgnore
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<Workspace> ownedWorkspaces;

    @JsonIgnore
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<WorkspaceMember> workspaceMemberships;

    // ── Relationships với entity chưa tồn tại (mở comment khi tạo entity) ───

    // projects.created_by → users.user_id
    @JsonIgnore
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "createdBy", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<Project> createdProjects;

    // tasks.reporter_id → users.user_id
    // @JsonIgnore
    // @ToString.Exclude
    // @EqualsAndHashCode.Exclude
    // @OneToMany(mappedBy = "reporter", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    // List<Task> reportedTasks;

    // tasks.creator_id → users.user_id
    // @JsonIgnore
    // @ToString.Exclude
    // @EqualsAndHashCode.Exclude
    // @OneToMany(mappedBy = "creator", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    // List<Task> createdTasks;

    // task_assignees.user_id → users.user_id
    // @JsonIgnore
    // @ToString.Exclude
    // @EqualsAndHashCode.Exclude
    // @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    // List<TaskAssignee> taskAssignments;

    // comments.user_id → users.user_id
    // @JsonIgnore
    // @ToString.Exclude
    // @EqualsAndHashCode.Exclude
    // @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    // List<Comment> comments;

    // attachments.uploaded_by → users.user_id
    // @JsonIgnore
    // @ToString.Exclude
    // @EqualsAndHashCode.Exclude
    // @OneToMany(mappedBy = "uploadedBy", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    // List<Attachment> uploadedAttachments;

    // activity_logs.actor_id → users.user_id
    // @JsonIgnore
    // @ToString.Exclude
    // @EqualsAndHashCode.Exclude
    // @OneToMany(mappedBy = "actor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    // List<ActivityLog> activityLogs;
}
