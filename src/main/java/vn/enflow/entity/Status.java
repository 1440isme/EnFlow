package vn.enflow.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "statuses")
public class Status {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "status_id")
    Long statusId;

    @Column(name = "name", nullable = false, length = 100)
    String name;

    @Column(name = "status_group", nullable = false)
    @Enumerated(EnumType.STRING)
    StatusGroup statusGroup;

    @Column(name = "color", length = 20)
    String color;

    @Column(name = "position", nullable = false)
    Integer position = 0;

    @Column(name = "is_default", nullable = false)
    Boolean isDefault = false;

    // ── Enums ────────────────────────────────────────────────────────────────

    public enum StatusGroup {
        idea, backlog, to_do, in_progress, review, testing, deploy, completed
    }

    // ── Relationships với entity đã tồn tại ──────────────────────────────────

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "list_id", nullable = false)
    ProjectList list;

    // ── Relationships với entity chưa tồn tại (mở comment khi tạo entity) ───

    // tasks.status_id → statuses.status_id
    // @JsonIgnore
    // @ToString.Exclude
    // @EqualsAndHashCode.Exclude
    // @OneToMany(mappedBy = "status", cascade = CascadeType.ALL, fetch =
    // FetchType.LAZY)
    // List<Task> tasks;
}
