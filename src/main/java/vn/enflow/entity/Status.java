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

    @Column(name = "status_group", nullable = false)
    StatusGroup statusGroup;

    @Column(name = "color", length = 20)
    String color;

    @Column(name = "is_default", nullable = false)
    Boolean isDefault = false;

    // ── Enum có thứ tự cố định ───────────────────────────────────────────────

    public enum StatusGroup {
        IDEA(1, "IDEA"),
        BACKLOG(2, "BACKLOG"),
        TO_DO(3, "TO DO"),
        IN_PROGRESS(4, "IN PROGRESS"),
        REVIEW(5, "REVIEW"),
        TESTING(6, "TESTING"),
        DEPLOY(7, "DEPLOY"),
        COMPLETED(8, "COMPLETED");

        private final int order;
        private final String displayName;

        StatusGroup(int order, String displayName) {
            this.order = order;
            this.displayName = displayName;
        }

        public int getOrder() {
            return order;
        }

        public String getDisplayName() {
            return displayName;
        }

        public static StatusGroup fromString(String value) {
            if (value == null || value.isBlank()) {
                return null;
            }

            String candidate = value.trim();
            for (StatusGroup statusGroup : StatusGroup.values()) {
                if (statusGroup.name().equalsIgnoreCase(candidate)
                        || statusGroup.displayName.equalsIgnoreCase(candidate)) {
                    return statusGroup;
                }
            }

            return null;
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