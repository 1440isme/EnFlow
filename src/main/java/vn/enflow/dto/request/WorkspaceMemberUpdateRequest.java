package vn.enflow.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.enflow.entity.WorkspaceMember;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkspaceMemberUpdateRequest {
    private WorkspaceMember.RoleInWorkspace roleInWorkspace;
    private Boolean isActive;
}
