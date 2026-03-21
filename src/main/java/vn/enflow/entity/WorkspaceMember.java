package vn.enflow.entity;

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
@Table(name = "workspace_members")
public class WorkspaceMember {

    @EmbeddedId
    WorkspaceMemberId id;

    // ── Relationships với entity đã tồn tại ──────────────────────────────────

    // workspace_members.workspace_id → workspaces.workspace_id
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @MapsId("workspaceId")
    @JoinColumn(name = "workspace_id")
    Workspace workspace;

    // workspace_members.user_id → users.user_id
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "role_in_workspace", nullable = false)
    RoleInWorkspace roleInWorkspace;

    @Column(name = "joined_at", nullable = false)
    LocalDateTime joinedAt;

    @Column(name = "is_active", nullable = false)
    Boolean isActive = true;

    public enum RoleInWorkspace {
        owner, admin, member, guest
    }
}
