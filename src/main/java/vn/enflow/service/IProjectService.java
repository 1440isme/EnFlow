package vn.enflow.service;

import vn.enflow.dto.request.ProjectCreatetionRequest;
import vn.enflow.dto.respone.ProjectListStatusesResponse;
import vn.enflow.dto.respone.ProjectResponse;

import java.util.List;

public interface IProjectService {

    ProjectResponse createtionProject(Long idWorkspace, ProjectCreatetionRequest projectRequest);
    ProjectResponse getProjectById(Long projectId);
    ProjectListStatusesResponse getProjectListStatuses(Long projectId);
    List<ProjectResponse> getProjectsByWorkspaceId(Long workspaceId);
    ProjectResponse updateProject(Long projectId, ProjectCreatetionRequest projectRequest);
    void deleteProject(Long projectId);
}
