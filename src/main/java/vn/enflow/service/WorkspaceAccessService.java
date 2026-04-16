package vn.enflow.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import vn.enflow.entity.WorkspaceMember;
import vn.enflow.repository.TaskAssigneeRepository;
import vn.enflow.repository.WorkspaceMemberRepository;
import vn.enflow.security.SecurityUtils;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WorkspaceAccessService {

    WorkspaceMemberRepository workspaceMemberRepository;
    TaskAssigneeRepository taskAssigneeRepository;

    public WorkspaceMember requireActiveMembership(Long workspaceId, Long userId) {
        var member = workspaceMemberRepository
                .findById_WorkspaceIdAndId_UserId(workspaceId, userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "Không có quyền truy cập workspace"));
        if (!Boolean.TRUE.equals(member.getIsActive())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Không có quyền truy cập workspace");
        }
        return member;
    }

    /** Chặn Guest khỏi mọi thao tác ghi (POST/PUT/DELETE). */
    public WorkspaceMember requireWriteAccess(Long workspaceId, Long userId) {
        WorkspaceMember member = requireActiveMembership(workspaceId, userId);
        if (member.getRoleInWorkspace() == WorkspaceMember.RoleInWorkspace.guest) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Guest chỉ được xem");
        }
        return member;
    }

    public void requireCurrentUserActiveMember(Long workspaceId) {
        requireActiveMembership(workspaceId, SecurityUtils.currentUserId());
    }

    public void requireCurrentUserWriteAccess(Long workspaceId) {
        requireWriteAccess(workspaceId, SecurityUtils.currentUserId());
    }

    public WorkspaceMember requireOwnerAccess(Long workspaceId, Long userId) {
        WorkspaceMember member = requireActiveMembership(workspaceId, userId);
        if (member.getRoleInWorkspace() != WorkspaceMember.RoleInWorkspace.owner) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Chỉ chủ sở hữu mới được thực hiện thao tác này");
        }
        return member;
    }

    public void requireCurrentUserOwnerAccess(Long workspaceId) {
        requireOwnerAccess(workspaceId, SecurityUtils.currentUserId());
    }

    /**
     * Owner được thao tác mọi task trong workspace.
     * Member chỉ được thao tác trên task đã được assign cho chính mình.
     * Guest không có quyền ghi.
     */
    public WorkspaceMember requireTaskActionAccess(Long workspaceId, Long taskId, Long userId) {
        WorkspaceMember member = requireActiveMembership(workspaceId, userId);
        if (member.getRoleInWorkspace() == WorkspaceMember.RoleInWorkspace.owner) {
            return member;
        }
        if (member.getRoleInWorkspace() == WorkspaceMember.RoleInWorkspace.guest) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Guest chỉ được xem");
        }
        boolean assigned = taskAssigneeRepository.existsById_TaskIdAndId_UserId(taskId, userId);
        if (!assigned) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Member chỉ được thao tác trên task được giao cho chính mình");
        }
        return member;
    }

    public WorkspaceMember requireCurrentUserTaskActionAccess(Long workspaceId, Long taskId) {
        return requireTaskActionAccess(workspaceId, taskId, SecurityUtils.currentUserId());
    }
}
