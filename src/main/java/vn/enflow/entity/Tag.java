package vn.enflow.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "tags")
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tag_id")
    Long tagId;

    @Column(name = "name", nullable = false, length = 100)
    String name;

    @Column(name = "color", length = 20)
    String color;

    // ── Relationships với entity đã tồn tại ──────────────────────────────────

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workspace_id", nullable = false)
    Workspace workspace;

    @JsonIgnore
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "tag", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<TaskTag> taskTags;
}
