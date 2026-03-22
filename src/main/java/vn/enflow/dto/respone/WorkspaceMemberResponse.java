package vn.enflow.dto.respone;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.enflow.entity.WorkspaceMember;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkspaceMemberResponse {
    private Long workspaceId;
    private Long userId;
    private WorkspaceMember.RoleInWorkspace roleInWorkspace;
    private LocalDateTime joinedAt;
    private Boolean isActive;
}
