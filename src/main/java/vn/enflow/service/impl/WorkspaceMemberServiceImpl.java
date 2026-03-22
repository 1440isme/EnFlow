package vn.enflow.service.impl;

import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
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
import vn.enflow.service.IWorkspaceMemberService;

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

    @Override
    @Transactional
    public WorkspaceMemberResponse addMember(Long workspaceId, WorkspaceMemberRequest request) {
        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy workspace với id: " + workspaceId));

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy user với id: " + request.getUserId()));

        WorkspaceMemberId memberId = new WorkspaceMemberId(workspaceId, request.getUserId());
        if (workspaceMemberRepository.existsById(memberId)) {
            throw new RuntimeException("User đã là thành viên của workspace này");
        }

        WorkspaceMember.RoleInWorkspace role = request.getRoleInWorkspace() != null
                ? request.getRoleInWorkspace()
                : WorkspaceMember.RoleInWorkspace.member;

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
        WorkspaceMemberId memberId = new WorkspaceMemberId(workspaceId, userId);
        WorkspaceMember member = workspaceMemberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thành viên"));

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
        WorkspaceMemberId memberId = new WorkspaceMemberId(workspaceId, userId);
        if (!workspaceMemberRepository.existsById(memberId)) {
            throw new RuntimeException("Không tìm thấy thành viên");
        }
        workspaceMemberRepository.deleteById(memberId);
    }
}
