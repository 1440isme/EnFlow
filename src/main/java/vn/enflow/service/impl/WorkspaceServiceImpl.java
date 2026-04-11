package vn.enflow.service.impl;

import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import vn.enflow.dto.request.WorkspaceRequest;
import vn.enflow.dto.request.WorkspaceUpdateRequest;
import vn.enflow.dto.respone.WorkspaceResponse;
import vn.enflow.entity.User;
import vn.enflow.entity.Workspace;
import vn.enflow.entity.WorkspaceMember;
import vn.enflow.entity.WorkspaceMemberId;
import vn.enflow.mapper.WorkspaceMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import vn.enflow.repository.UserRepository;
import vn.enflow.repository.WorkspaceMemberRepository;
import vn.enflow.repository.WorkspaceRepository;
import vn.enflow.security.SecurityUtils;
import vn.enflow.service.IWorkspaceService;
import vn.enflow.service.WorkspaceAccessService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WorkspaceServiceImpl implements IWorkspaceService {

    WorkspaceRepository workspaceRepository;
    WorkspaceMemberRepository workspaceMemberRepository;
    UserRepository userRepository;
    WorkspaceMapper workspaceMapper;
    WorkspaceAccessService workspaceAccessService;

    @Override
    @Transactional
    public WorkspaceResponse create(WorkspaceRequest request) {
        Long currentUserId = SecurityUtils.currentUserId();
        if (request.getOwnerUserId() == null || !request.getOwnerUserId().equals(currentUserId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Chỉ được tạo workspace với bạn là chủ sở hữu");
        }
        return persistNewWorkspace(request);
    }

    @Override
    @Transactional
    public WorkspaceResponse createDuringRegistration(WorkspaceRequest request) {
        if (request.getOwnerUserId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ownerUserId là bắt buộc");
        }
        return persistNewWorkspace(request);
    }

    private WorkspaceResponse persistNewWorkspace(WorkspaceRequest request) {
        if (request.getWorkspaceKey() != null && workspaceRepository.existsByWorkspaceKey(request.getWorkspaceKey())) {
            throw new RuntimeException("Workspace key đã tồn tại");
        }

        User owner = userRepository.findById(request.getOwnerUserId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy user với id: " + request.getOwnerUserId()));

        Workspace workspace = workspaceMapper.toWorkspace(request);
        workspace.setOwner(owner);

        LocalDateTime now = LocalDateTime.now();
        workspace.setCreatedAt(now);
        workspace.setUpdatedAt(now);

        if (workspace.getIsPrivate() == null) {
            workspace.setIsPrivate(false);
        }

        Workspace saved = workspaceRepository.save(workspace);

        WorkspaceMember ownerMember = WorkspaceMember.builder()
                .id(new WorkspaceMemberId(saved.getWorkspaceId(), owner.getUserId()))
                .workspace(saved)
                .user(owner)
                .roleInWorkspace(WorkspaceMember.RoleInWorkspace.owner)
                .joinedAt(now)
                .isActive(true)
                .build();
        workspaceMemberRepository.save(ownerMember);

        return workspaceMapper.toWorkspaceResponse(saved);
    }

    @Override
    public WorkspaceResponse findById(Long id) {
        Workspace workspace = workspaceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy workspace với id: " + id));
        workspaceAccessService.requireActiveMembership(id, SecurityUtils.currentUserId());
        return workspaceMapper.toWorkspaceResponse(workspace);
    }

    @Override
    public List<WorkspaceResponse> findAccessibleForCurrentUser() {
        Long userId = SecurityUtils.currentUserId();
        return workspaceRepository.findAllByActiveMemberUserId(userId).stream()
                .map(workspaceMapper::toWorkspaceResponse)
                .toList();
    }

    @Override
    public List<WorkspaceResponse> findByOwner(Long ownerUserId) {
        return workspaceRepository.findByOwner_UserId(ownerUserId).stream()
                .map(workspaceMapper::toWorkspaceResponse)
                .toList();
    }

    @Override
    @Transactional
    public WorkspaceResponse update(Long id, WorkspaceUpdateRequest request) {
        Workspace workspace = workspaceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy workspace với id: " + id));
        if (!workspace.getOwner().getUserId().equals(SecurityUtils.currentUserId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Chỉ chủ sở hữu mới chỉnh sửa workspace");
        }

        workspaceMapper.updateWorkspace(workspace, request);
        workspace.setUpdatedAt(LocalDateTime.now());

        return workspaceMapper.toWorkspaceResponse(workspaceRepository.save(workspace));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Workspace workspace = workspaceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy workspace với id: " + id));
        if (!workspace.getOwner().getUserId().equals(SecurityUtils.currentUserId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Chỉ chủ sở hữu mới xóa workspace");
        }
        workspaceRepository.deleteById(id);
    }
}
