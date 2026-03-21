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
@Table(name = "tasks")
public class Task {

    

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_id")
    Long taskId;

    @Column(name = "task_code", nullable = true, length = 50)
    String taskCode;

    @Column(name = "title", nullable = false, columnDefinition = "NVARCHAR(255)")
    String title;

    @Column(name = "description", columnDefinition = "NVARCHAR(MAX)")
    String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "task_type", nullable = false)
    TaskType taskType = TaskType.task;

    @Enumerated(EnumType.STRING)
    @Column(name = "priority", nullable = false)
    Priority priority = Priority.normal;

    @Column(name = "start_date")
    LocalDateTime startDate;

    @Column(name = "due_date")
    LocalDateTime dueDate;

    @Column(name = "completed_at")
    LocalDateTime completedAt;

    @Column(name = "resolution", columnDefinition = "NVARCHAR(MAX)")
    String resolution;

    @Column(name = "time_estimate_days")
    Double timeEstimateDays;

    @Column(name = "time_spent_days")
    Double timeSpentDays;

    @Column(name = "points")
    Integer points;

    @Column(name = "position")
    Integer position = 0;

    @Column(name = "is_private")
    Boolean isPrivate = false;

    @Column(name = "archived")
    Boolean archived = false;

    @Column(name = "created_at", nullable = false)
    LocalDateTime createdAt;

    @Column(name = "updated_at")
    LocalDateTime updatedAt;

    // ── Enums ────────────────────────────────────────────────────────────────

    public enum TaskType {
        epic, story, task, bug, subtask
    }

    public enum Priority {
        low, normal, high, urgent
    }

    // ── Relationships với entity đã tồn tại ──────────────────────────────────

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "list_id", nullable = false)
    ProjectList list;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id", nullable = false)
    Status status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_task_id", nullable = false)
    Task parentTask;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reporter_id", nullable = false)
    User reporter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id", nullable = false)
    User creator;


    @JsonIgnore
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "parentTask", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<Task> subtasks;

    @JsonIgnore
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<TaskAssignee> assignees;

    @JsonIgnore
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<TaskTag> taskTags;

    // ── Relationships với entity chưa tồn tại (mở comment khi tạo entity) ───

    // comments.task_id → tasks.task_id
    @JsonIgnore
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<Comment> comments;

    // attachments.task_id → tasks.task_id
    @JsonIgnore
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<Attachment> attachments;

    // activity_logs.task_id → tasks.task_id
    @JsonIgnore
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<ActivityLog> activityLogs;
}
