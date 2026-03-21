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
@Table(name = "lists")
public class ProjectList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "list_id")
    Long listId;

    @Column(name = "name", nullable = false)
    String name;

    @Column(name = "description", columnDefinition = "TEXT")
    String description;

    @Column(name = "position", nullable = false)
    Integer position = 0;

    @Column(name = "is_private", nullable = false)
    Boolean isPrivate = false;

    @Column(name = "archived", nullable = false)
    Boolean archived = false;

    @Column(name = "created_at", nullable = false)
    LocalDateTime createdAt;

    @Column(name = "updated_at")
    LocalDateTime updatedAt;

    // ── Relationships với entity đã tồn tại ──────────────────────────────────

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    Project project;

    @JsonIgnore
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "list", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<Status> statuses;

    // ── Relationships với entity chưa tồn tại (mở comment khi tạo entity) ───

    // tasks.list_id → lists.list_id
    // @JsonIgnore
    // @ToString.Exclude
    // @EqualsAndHashCode.Exclude
    // @OneToMany(mappedBy = "list", cascade = CascadeType.ALL, fetch =
    // FetchType.LAZY)
    // List<Task> tasks;
}
