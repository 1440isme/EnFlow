package vn.enflow.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Entity
@Table(name = "statuses")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Status {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "status_id")
    Long statusId;

    @Column(name = "name", nullable = false, length = 100)
    String name;

    @Column(name = "status_group", nullable = false)
    StatusGroup statusGroup;

    @Column(name = "color", length = 20)
    String color;

    @Column(name = "is_default", nullable = false)
    Boolean isDefault = false;

    // ── Enum có thứ tự cố định ───────────────────────────────────────────────

    public enum StatusGroup {
        IDEA(1),
        BACKLOG(2),
        TO_DO(3),
        IN_PROGRESS(4),
        REVIEW(5),
        TESTING(6),
        DEPLOY(7),
        COMPLETED(8);

        private final int order;

        StatusGroup(int order) {
            this.order = order;
        }

        public int getOrder() {
            return order;
        }

        public static StatusGroup fromString(String value) {
            if (value == null) return null;
            // Handle lowercase, spaces, dashes and 'todo' vs 'to_do'
            String normalized = value.trim().toUpperCase()
                    .replace("-", "_")
                    .replace(" ", "_");

            if (normalized.equals("TODO")) return TO_DO;

            try {
                return StatusGroup.valueOf(normalized);
            } catch (IllegalArgumentException e) {
                // Return a default or handle errors
                return null;
            }
        }
    }

    @Converter(autoApply = true)
    public static class StatusGroupConverter implements AttributeConverter<StatusGroup, String> {
        @Override
        public String convertToDatabaseColumn(StatusGroup attribute) {
            return attribute == null ? null : attribute.name();
        }

        @Override
        public StatusGroup convertToEntityAttribute(String dbData) {
            return StatusGroup.fromString(dbData);
        }
    }

    // ── Relationships ────────────────────────────────────────────────────────

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "list_id", nullable = false)
    ProjectList list;

    @JsonIgnore
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "status", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<Task> tasks;
}