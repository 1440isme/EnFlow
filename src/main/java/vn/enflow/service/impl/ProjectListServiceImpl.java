package vn.enflow.service.impl;

import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import vn.enflow.dto.request.ProjectListCreatetionRequest;
import vn.enflow.dto.request.ProjectListUpdateRequest;
import vn.enflow.dto.respone.ProjectListResponse;
import vn.enflow.entity.Project;
import vn.enflow.entity.ProjectList;
import vn.enflow.entity.Status;
import vn.enflow.mapper.ProjectListMapper;
import vn.enflow.repository.ProjectListRepository;
import vn.enflow.repository.ProjectRepository;
import vn.enflow.repository.StatusRepository;
import vn.enflow.security.SecurityUtils;
import vn.enflow.service.IProjectListService;
import vn.enflow.service.WorkspaceAccessService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProjectListServiceImpl implements IProjectListService {

    ProjectListRepository projectListRepository;
    ProjectRepository projectRepository;
    ProjectListMapper projectListMapper;
    StatusRepository statusRepository;
    WorkspaceAccessService workspaceAccessService;

    @Override
    @Transactional
    public ProjectListResponse createtionProjectList(Long projectId, ProjectListCreatetionRequest request) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy project với id: " + projectId));
        workspaceAccessService.requireOwnerAccess(project.getWorkspace().getWorkspaceId(), SecurityUtils.currentUserId());

        ProjectList projectList = projectListMapper.toProjectList(request);
        projectList.setProject(project);

        LocalDateTime now = LocalDateTime.now();
        projectList.setCreatedAt(now);
        projectList.setUpdatedAt(now);

        if (projectList.getPosition() == null)
            projectList.setPosition(0);
        if (projectList.getIsPrivate() == null)
            projectList.setIsPrivate(false);
        if (projectList.getArchived() == null)
            projectList.setArchived(false);

        ProjectList saved = projectListRepository.save(projectList);

        Status toDo = Status.builder()
                .statusGroup(Status.StatusGroup.TO_DO)
                .color("#9CA3AF")
                .isDefault(true)
                .project(saved.getProject())
                .list(saved)
                .build();

        Status inProgress = Status.builder()
                .statusGroup(Status.StatusGroup.IN_PROGRESS)
                .color("#3B82F6")
                .isDefault(true)
                .project(saved.getProject())
                .list(saved)
                .build();

        Status completed = Status.builder()
                .statusGroup(Status.StatusGroup.COMPLETED)
                .color("#10B981")
                .isDefault(true)
                .project(saved.getProject())
                .list(saved)
                .build();

        statusRepository.saveAll(List.of(toDo, inProgress, completed));

        return projectListMapper.toProjectListResponse(saved);
    }

    @Override
    public ProjectListResponse getProjectListById(Long listId) {
        ProjectList projectList = projectListRepository.findById(listId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy list với id: " + listId));
        workspaceAccessService.requireCurrentUserActiveMember(projectList.getProject().getWorkspace().getWorkspaceId());
        return projectListMapper.toProjectListResponse(projectList);
    }

    @Override
    public List<ProjectListResponse> getProjectListsByProjectId(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy project với id: " + projectId));
        workspaceAccessService.requireCurrentUserActiveMember(project.getWorkspace().getWorkspaceId());
        return projectListRepository.findByProject_ProjectId(projectId).stream()
                .map(projectListMapper::toProjectListResponse)
                .toList();
    }

    @Override
    @Transactional
    public ProjectListResponse updateProjectList(Long listId, ProjectListUpdateRequest request) {
        ProjectList projectList = projectListRepository.findById(listId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy list với id: " + listId));
        workspaceAccessService.requireOwnerAccess(projectList.getProject().getWorkspace().getWorkspaceId(), SecurityUtils.currentUserId());

        projectListMapper.updateProjectList(projectList, request);
        projectList.setUpdatedAt(LocalDateTime.now());

        return projectListMapper.toProjectListResponse(projectListRepository.save(projectList));
    }

    @Override
    @Transactional
    public void deleteProjectList(Long listId) {
        ProjectList projectList = projectListRepository.findById(listId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy list với id: " + listId));
        workspaceAccessService.requireOwnerAccess(projectList.getProject().getWorkspace().getWorkspaceId(), SecurityUtils.currentUserId());
        projectListRepository.deleteById(listId);
    }
}
