package vn.enflow.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import vn.enflow.entity.WorkspaceMember;
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

    public WorkspaceMember requireActiveMembership(Long workspaceId, Long userId) {
        var member = workspaceMemberRepository
                .findById_WorkspaceIdAndId_UserId(workspaceId, userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "Không có quyền truy cập workspace"));
        if (!Boolean.TRUE.equals(member.getIsActive())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Không có quyền truy cập workspace");
        }
        return member;
    }

    public void requireCurrentUserActiveMember(Long workspaceId) {
        requireActiveMembership(workspaceId, SecurityUtils.currentUserId());
    }
}
