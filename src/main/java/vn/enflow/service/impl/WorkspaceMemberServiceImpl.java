package vn.enflow.service.impl;

import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import vn.enflow.dto.request.WorkspaceMemberRequest;
import vn.enflow.dto.request.WorkspaceMemberUpdateRequest;
import vn.enflow.dto.respone.WorkspaceMemberResponse;
import vn.enflow.entity.User;
import vn.enflow.entity.Workspace;
import vn.enflow.entity.WorkspaceMember;
import vn.enflow.entity.WorkspaceMemberId;
import vn.enflow.mapper.WorkspaceMemberMapper;
import vn.enflow.repository.UserRepository;
import vn.enflow.repository.WorkspaceMemberRepository;
import vn.enflow.repository.WorkspaceRepository;
import vn.enflow.security.SecurityUtils;
import vn.enflow.service.IWorkspaceMemberService;
import vn.enflow.service.WorkspaceAccessService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WorkspaceMemberServiceImpl implements IWorkspaceMemberService {

    WorkspaceMemberRepository workspaceMemberRepository;
    WorkspaceRepository workspaceRepository;
    UserRepository userRepository;
    WorkspaceMemberMapper workspaceMemberMapper;
    WorkspaceAccessService workspaceAccessService;

    @Override
    @Transactional
    public WorkspaceMemberResponse addMember(Long workspaceId, WorkspaceMemberRequest request) {
        Long actorId = SecurityUtils.currentUserId();
        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy workspace với id: " + workspaceId));

        WorkspaceMember actor = workspaceAccessService.requireActiveMembership(workspaceId, actorId);
        assertCanInviteMembers(actor);

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy user với id: " + request.getUserId()));

        WorkspaceMemberId memberId = new WorkspaceMemberId(workspaceId, request.getUserId());
        if (workspaceMemberRepository.existsById(memberId)) {
            throw new RuntimeException("User đã là thành viên của workspace này");
        }

        WorkspaceMember.RoleInWorkspace role = request.getRoleInWorkspace() != null
                ? request.getRoleInWorkspace()
                : WorkspaceMember.RoleInWorkspace.member;

        if (role == WorkspaceMember.RoleInWorkspace.owner) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Không thể gán vai trò chủ sở hữu khi mời");
        }

        assertOwnerOnlyAssignsRoles(actor, role);

        WorkspaceMember member = WorkspaceMember.builder()
                .id(memberId)
                .workspace(workspace)
                .user(user)
                .roleInWorkspace(role)
                .joinedAt(LocalDateTime.now())
                .isActive(true)
                .build();

        return workspaceMemberMapper.toWorkspaceMemberResponse(workspaceMemberRepository.save(member));
    }

    @Override
    public List<WorkspaceMemberResponse> getMembersByWorkspace(Long workspaceId) {
        workspaceAccessService.requireCurrentUserActiveMember(workspaceId);
        if (!workspaceRepository.existsById(workspaceId)) {
            throw new RuntimeException("Không tìm thấy workspace với id: " + workspaceId);
        }
        return workspaceMemberRepository.findById_WorkspaceId(workspaceId).stream()
                .map(workspaceMemberMapper::toWorkspaceMemberResponse)
                .toList();
    }

    @Override
    @Transactional
    public WorkspaceMemberResponse updateMember(Long workspaceId, Long userId, WorkspaceMemberUpdateRequest request) {
        Long actorId = SecurityUtils.currentUserId();
        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy workspace với id: " + workspaceId));

        WorkspaceMember actor = workspaceAccessService.requireActiveMembership(workspaceId, actorId);
        assertCanManageMembers(actor);

        WorkspaceMemberId memberId = new WorkspaceMemberId(workspaceId, userId);
        WorkspaceMember member = workspaceMemberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thành viên"));

        Long ownerUserId = workspace.getOwner().getUserId();
        boolean targetIsOwnerUser = userId.equals(ownerUserId);

        if (member.getRoleInWorkspace() == WorkspaceMember.RoleInWorkspace.owner) {
            if (request.getRoleInWorkspace() != null
                    && request.getRoleInWorkspace() != WorkspaceMember.RoleInWorkspace.owner) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Không thể thay đổi vai trò chủ sở hữu");
            }
        }

        if (targetIsOwnerUser && actor.getRoleInWorkspace() != WorkspaceMember.RoleInWorkspace.owner) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Không thể chỉnh tài khoản chủ sở hữu");
        }

        if (request.getRoleInWorkspace() != null) {
            if (request.getRoleInWorkspace() == WorkspaceMember.RoleInWorkspace.owner) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Không thể gán vai trò chủ sở hữu qua API");
            }
            assertOwnerOnlyAssignsRoles(actor, request.getRoleInWorkspace());
        }

        if (request.getRoleInWorkspace() != null) {
            member.setRoleInWorkspace(request.getRoleInWorkspace());
        }
        if (request.getIsActive() != null) {
            member.setIsActive(request.getIsActive());
        }

        return workspaceMemberMapper.toWorkspaceMemberResponse(workspaceMemberRepository.save(member));
    }

    @Override
    @Transactional
    public void removeMember(Long workspaceId, Long userId) {
        Long actorId = SecurityUtils.currentUserId();
        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy workspace với id: " + workspaceId));

        WorkspaceMember actor = workspaceAccessService.requireActiveMembership(workspaceId, actorId);
        assertCanManageMembers(actor);

        WorkspaceMemberId memberId = new WorkspaceMemberId(workspaceId, userId);
        WorkspaceMember target = workspaceMemberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thành viên"));

        Long ownerUserId = workspace.getOwner().getUserId();
        if (userId.equals(ownerUserId) || target.getRoleInWorkspace() == WorkspaceMember.RoleInWorkspace.owner) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Không thể gỡ chủ sở hữu khỏi workspace");
        }

        workspaceMemberRepository.deleteById(memberId);
    }

    /** Chỉ chủ sở hữu mới thêm thành viên. */
    private static void assertCanInviteMembers(WorkspaceMember actor) {
        if (actor.getRoleInWorkspace() != WorkspaceMember.RoleInWorkspace.owner) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Chỉ chủ sở hữu mới thêm thành viên");
        }
    }

    /** Chỉ chủ sở hữu mới đổi vai trò / trạng thái thành viên hoặc gỡ thành viên. */
    private static void assertCanManageMembers(WorkspaceMember actor) {
        if (actor.getRoleInWorkspace() != WorkspaceMember.RoleInWorkspace.owner) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Chỉ chủ sở hữu mới quản lý thành viên");
        }
    }

    /** Chỉ owner gọi; gán được member hoặc guest. */
    private static void assertOwnerOnlyAssignsRoles(
            WorkspaceMember actor,
            WorkspaceMember.RoleInWorkspace assign) {
        if (actor.getRoleInWorkspace() != WorkspaceMember.RoleInWorkspace.owner) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Chỉ chủ sở hữu mới gán vai trò");
        }
        if (assign != WorkspaceMember.RoleInWorkspace.member
                && assign != WorkspaceMember.RoleInWorkspace.guest) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Vai trò không hợp lệ");
        }
    }
}
