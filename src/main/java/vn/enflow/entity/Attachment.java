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
@Table(name = "attachments")
public class Attachment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "attachment_id")
    Long attachmentId;

    @Column(name = "task_id", nullable = false)
    Long taskId;

    @Column(name = "uploaded_by", nullable = false)
    Long uploadedBy;

    @Column(name = "file_name")
    String fileName;

    @Column(name = "file_url")
    String fileUrl;

    @Column(name = "mime_type")
    String mimeType;

    @Column(name = "created_at", nullable = false)
    LocalDateTime createdAt;

    // ── Relationships với entity đã tồn tại ──────────────────────────────────

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id", referencedColumnName = "task_id", insertable = false, updatable = false)
    @JsonIgnore
    Task task;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uploaded_by", referencedColumnName = "user_id", insertable = false, updatable = false)
    @JsonIgnore
    User uploader;
}
