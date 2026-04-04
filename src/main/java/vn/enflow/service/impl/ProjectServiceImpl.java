package vn.enflow.service.impl;

import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import vn.enflow.dto.request.ProjectCreatetionRequest;
import vn.enflow.dto.respone.ProjectListResponse;
import vn.enflow.dto.respone.ProjectListStatusesResponse;
import vn.enflow.dto.respone.ProjectListWithStatusesResponse;
import vn.enflow.dto.respone.ProjectResponse;
import vn.enflow.dto.respone.StatusesRespone;
import vn.enflow.entity.Project;
import vn.enflow.entity.User;
import vn.enflow.entity.Workspace;
import vn.enflow.entity.ProjectList;
import vn.enflow.entity.Status;
import vn.enflow.mapper.ProjectListMapper;
import vn.enflow.mapper.ProjectMapper;
import vn.enflow.mapper.StatusMapper;
import vn.enflow.repository.ProjectRepository;
import vn.enflow.repository.WorkspaceRepository;
import vn.enflow.service.WorkspaceAccessService;
import vn.enflow.repository.ProjectListRepository;
import vn.enflow.repository.StatusRepository;
import vn.enflow.service.IProjectService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProjectServiceImpl implements IProjectService {

    ProjectRepository projectRepository;
    WorkspaceRepository workspaceRepository;
    WorkspaceAccessService workspaceAccessService;
    ProjectMapper projectMapper;
    ProjectListMapper projectListMapper;
    StatusMapper statusMapper;
    ProjectListRepository projectListRepository;
    StatusRepository statusRepository;

    @Override
    @Transactional
    public ProjectResponse createtionProject(Long workspaceId, ProjectCreatetionRequest request) {
        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy workspace với id: " + workspaceId));
        workspaceAccessService.requireCurrentUserActiveMember(workspaceId);

        if (request.getProjectKey() != null && projectRepository.existsByProjectKey(request.getProjectKey())) {
            throw new RuntimeException("Project key đã tồn tại: " + request.getProjectKey());
        }

        // createdBy: lấy owner của workspace làm người tạo (hoặc cung cấp qua request
        // nếu cần)
        User createdBy = workspace.getOwner();

        Project project = projectMapper.toProject(request);
        project.setWorkspace(workspace);
        project.setCreatedBy(createdBy);

        LocalDateTime now = LocalDateTime.now();
        project.setCreatedAt(now);
        project.setUpdatedAt(now);

        if (project.getIsPrivate() == null)
            project.setIsPrivate(false);
        if (project.getArchived() == null)
            project.setArchived(false);

        Project saved = projectRepository.save(project);

        // Tạo mặc định 1 ProjectList tên "List" cho project vừa tạo
        ProjectList defaultList = ProjectList.builder()
                .name("List")
                .description(null)
                .position(0)
                .isPrivate(false)
                .archived(false)
                .createdAt(now)
                .updatedAt(now)
                .project(saved)
                .build();

        ProjectList savedList = projectListRepository.save(defaultList);

        // Tạo 3 Status mặc định cho list: To do, In-progress, Completed
        Status toDo = Status.builder()
                .statusGroup(Status.StatusGroup.TO_DO)
                .color(null)
                .isDefault(true)
                .project(saved)
                .list(savedList)
                .build();

        Status inProgress = Status.builder()
                .statusGroup(Status.StatusGroup.IN_PROGRESS)
                .color(null)
                .isDefault(true)
                .project(saved)
                .list(savedList)
                .build();

        Status completed = Status.builder()
                .statusGroup(Status.StatusGroup.COMPLETED)
                .color(null)
                .isDefault(true)
                .project(saved)
                .list(savedList)
                .build();

        statusRepository.saveAll(List.of(toDo, inProgress, completed));

        return projectMapper.toProjectResponse(saved);
    }

    @Override
    public ProjectResponse getProjectById(Long projectId) {
        Long wsId = projectRepository.findWorkspaceIdByProjectId(projectId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy project với id: " + projectId));
        workspaceAccessService.requireCurrentUserActiveMember(wsId);
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy project với id: " + projectId));
        return projectMapper.toProjectResponse(project);
    }

    @Override
    public ProjectListStatusesResponse getProjectListStatuses(Long projectId) {
        Long wsId = projectRepository.findWorkspaceIdByProjectId(projectId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy project với id: " + projectId));
        workspaceAccessService.requireCurrentUserActiveMember(wsId);

        List<ProjectList> listEntities = projectListRepository.findByProject_ProjectId(projectId);
        listEntities.sort(Comparator
                .comparing(ProjectList::getPosition, Comparator.nullsLast(Integer::compareTo))
                .thenComparing(ProjectList::getListId));

        List<Status> statusEntities = statusRepository.findByProject_ProjectId(projectId);
        Map<Long, List<StatusesRespone>> statusesByListId = new HashMap<>();
        statusEntities.forEach((status) -> {
            Long listId = status.getList() != null ? status.getList().getListId() : null;
            if (listId == null)
                return;
            statusesByListId
                    .computeIfAbsent(listId, ignored -> new ArrayList<>())
                    .add(statusMapper.toStatusesRespone(status));
        });
        statusesByListId.values().forEach((items) -> items.sort(Comparator
                .comparing(StatusesRespone::getPosition, Comparator.nullsLast(Integer::compareTo))
                .thenComparing(StatusesRespone::getStatusId)));

        List<ProjectListWithStatusesResponse> lists = listEntities.stream().map((listEntity) -> {
            ProjectListResponse base = projectListMapper.toProjectListResponse(listEntity);
            return ProjectListWithStatusesResponse.builder()
                    .listProjectId(base.getListProjectId())
                    .name(base.getName())
                    .description(base.getDescription())
                    .position(base.getPosition())
                    .isPrivate(base.getIsPrivate())
                    .archived(base.getArchived())
                    .createdAt(base.getCreatedAt())
                    .updatedAt(base.getUpdatedAt())
                    .projectId(base.getProjectId())
                    .statuses(statusesByListId.getOrDefault(base.getListProjectId(), List.of()))
                    .build();
        }).toList();

        return ProjectListStatusesResponse.builder()
                .projectId(projectId)
                .lists(lists)
                .build();
    }

    @Override
    public List<ProjectResponse> getProjectsByWorkspaceId(Long workspaceId) {
        if (!workspaceRepository.existsById(workspaceId)) {
            throw new RuntimeException("Không tìm thấy workspace với id: " + workspaceId);
        }
        workspaceAccessService.requireCurrentUserActiveMember(workspaceId);
        return projectRepository.findByWorkspace_WorkspaceId(workspaceId).stream()
                .map(projectMapper::toProjectResponse)
                .toList();
    }

    @Override
    @Transactional
    public ProjectResponse updateProject(Long projectId, ProjectCreatetionRequest request) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy project với id: " + projectId));
        workspaceAccessService.requireCurrentUserActiveMember(project.getWorkspace().getWorkspaceId());

        projectMapper.updateProject(project, request);
        project.setUpdatedAt(LocalDateTime.now());

        return projectMapper.toProjectResponse(projectRepository.save(project));
    }

    @Override
    @Transactional
    public void deleteProject(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy project với id: " + projectId));
        workspaceAccessService.requireCurrentUserActiveMember(project.getWorkspace().getWorkspaceId());
        projectRepository.deleteById(projectId);
    }
}
