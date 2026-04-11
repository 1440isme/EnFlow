package vn.enflow.service.impl;

import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import vn.enflow.dto.request.StatusesCreatetionRequest;
import vn.enflow.dto.request.StatusesUpdateRequest;
import vn.enflow.dto.respone.StatusesRespone;
import vn.enflow.entity.Project;
import vn.enflow.entity.ProjectList;
import vn.enflow.entity.Status;
import vn.enflow.mapper.StatusMapper;
import vn.enflow.repository.ProjectListRepository;
import vn.enflow.repository.ProjectRepository;
import vn.enflow.repository.StatusRepository;
import vn.enflow.security.SecurityUtils;
import vn.enflow.service.IStatusesService;
import vn.enflow.service.WorkspaceAccessService;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StatusesServiceImpl implements IStatusesService {

    StatusRepository statusRepository;
    ProjectRepository projectRepository;
    ProjectListRepository projectListRepository;
    StatusMapper statusMapper;
    WorkspaceAccessService workspaceAccessService;

    @Override
    @Transactional
    public StatusesRespone createtionStatus(Long projectId, Long listId, StatusesCreatetionRequest request) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy project với id: " + projectId));
        workspaceAccessService.requireWriteAccess(project.getWorkspace().getWorkspaceId(), SecurityUtils.currentUserId());

        ProjectList list = projectListRepository.findById(listId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy list với id: " + listId));

        // Validate rằng list thuộc project
        if (!list.getProject().getProjectId().equals(projectId)) {
            throw new RuntimeException("List " + listId + " không thuộc project " + projectId);
        }

        Status status = statusMapper.toStatus(request);
        status.setProject(project);
        status.setList(list);

        if (status.getIsDefault() == null)
            status.setIsDefault(false);

        Status saved = statusRepository.save(status);
        return statusMapper.toStatusesRespone(saved);
    }

    @Override
    public StatusesRespone getStatusById(Long statusId) {
        Status status = statusRepository.findById(statusId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy status với id: " + statusId));
        return statusMapper.toStatusesRespone(status);
    }

    @Override
    public List<StatusesRespone> getStatusesByProjectId(Long projectId) {
        if (!projectRepository.existsById(projectId)) {
            throw new RuntimeException("Không tìm thấy project với id: " + projectId);
        }
        return statusRepository.findByProject_ProjectId(projectId).stream()
                .map(statusMapper::toStatusesRespone)
                .sorted((s1, s2) -> s1.getPosition().compareTo(s2.getPosition()))
                .toList();
    }

    @Override
    public List<StatusesRespone> getStatusesByListId(Long listId) {
        if (!projectListRepository.existsById(listId)) {
            throw new RuntimeException("Không tìm thấy list với id: " + listId);
        }
        return statusRepository.findByList_ListId(listId).stream()
                .map(statusMapper::toStatusesRespone)
                .sorted((s1, s2) -> s1.getPosition().compareTo(s2.getPosition()))
                .toList();
    }

    @Override
    @Transactional
    public StatusesRespone updateStatus(Long statusId, StatusesUpdateRequest request) {
        Status status = statusRepository.findById(statusId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy status với id: " + statusId));
        workspaceAccessService.requireWriteAccess(status.getProject().getWorkspace().getWorkspaceId(), SecurityUtils.currentUserId());

        statusMapper.updateStatus(status, request);

        return statusMapper.toStatusesRespone(statusRepository.save(status));
    }

    @Override
    @Transactional
    public void deleteStatus(Long statusId) {
        Status status = statusRepository.findById(statusId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy status với id: " + statusId));
        workspaceAccessService.requireWriteAccess(status.getProject().getWorkspace().getWorkspaceId(), SecurityUtils.currentUserId());
        statusRepository.deleteById(statusId);
    }
}
