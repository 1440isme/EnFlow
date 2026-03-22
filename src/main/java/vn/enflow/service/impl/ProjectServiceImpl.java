package vn.enflow.service.impl;

import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import vn.enflow.dto.request.ProjectCreatetionRequest;
import vn.enflow.dto.respone.ProjectResponse;
import vn.enflow.entity.Project;
import vn.enflow.entity.User;
import vn.enflow.entity.Workspace;
import vn.enflow.mapper.ProjectMapper;
import vn.enflow.repository.ProjectRepository;
import vn.enflow.repository.UserRepository;
import vn.enflow.repository.WorkspaceRepository;
import vn.enflow.service.IProjectService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProjectServiceImpl implements IProjectService {

    ProjectRepository projectRepository;
    WorkspaceRepository workspaceRepository;
    UserRepository userRepository;
    ProjectMapper projectMapper;

    @Override
    @Transactional
    public ProjectResponse createtionProject(Long workspaceId, ProjectCreatetionRequest request) {
        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy workspace với id: " + workspaceId));

        if (request.getProjectKey() != null && projectRepository.existsByProjectKey(request.getProjectKey())) {
            throw new RuntimeException("Project key đã tồn tại: " + request.getProjectKey());
        }

        // createdBy: lấy owner của workspace làm người tạo (hoặc cung cấp qua request nếu cần)
        User createdBy = workspace.getOwner();

        Project project = projectMapper.toProject(request);
        project.setWorkspace(workspace);
        project.setCreatedBy(createdBy);

        LocalDateTime now = LocalDateTime.now();
        project.setCreatedAt(now);
        project.setUpdatedAt(now);

        if (project.getIsPrivate() == null) project.setIsPrivate(false);
        if (project.getArchived() == null) project.setArchived(false);

        Project saved = projectRepository.save(project);
        return projectMapper.toProjectResponse(saved);
    }

    @Override
    public ProjectResponse getProjectById(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy project với id: " + projectId));
        return projectMapper.toProjectResponse(project);
    }

    @Override
    public List<ProjectResponse> getProjectsByWorkspaceId(Long workspaceId) {
        if (!workspaceRepository.existsById(workspaceId)) {
            throw new RuntimeException("Không tìm thấy workspace với id: " + workspaceId);
        }
        return projectRepository.findByWorkspace_WorkspaceId(workspaceId).stream()
                .map(projectMapper::toProjectResponse)
                .toList();
    }

    @Override
    @Transactional
    public ProjectResponse updateProject(Long projectId, ProjectCreatetionRequest request) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy project với id: " + projectId));

        projectMapper.updateProject(project, request);
        project.setUpdatedAt(LocalDateTime.now());

        return projectMapper.toProjectResponse(projectRepository.save(project));
    }

    @Override
    @Transactional
    public void deleteProject(Long projectId) {
        if (!projectRepository.existsById(projectId)) {
            throw new RuntimeException("Không tìm thấy project với id: " + projectId);
        }
        projectRepository.deleteById(projectId);
    }
}
