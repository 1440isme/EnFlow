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
import vn.enflow.repository.UserRepository;
import vn.enflow.repository.WorkspaceMemberRepository;
import vn.enflow.repository.WorkspaceRepository;
import vn.enflow.service.IWorkspaceService;

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

    @Override
    @Transactional
    public WorkspaceResponse create(WorkspaceRequest request) {
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

        // Tự động thêm owner vào workspace_members với role = owner
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
        return workspaceMapper.toWorkspaceResponse(workspace);
    }

    @Override
    public List<WorkspaceResponse> findAll() {
        return workspaceRepository.findAll().stream()
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

        workspaceMapper.updateWorkspace(workspace, request);
        workspace.setUpdatedAt(LocalDateTime.now());

        return workspaceMapper.toWorkspaceResponse(workspaceRepository.save(workspace));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!workspaceRepository.existsById(id)) {
            throw new RuntimeException("Không tìm thấy workspace với id: " + id);
        }
        workspaceRepository.deleteById(id);
    }
}
