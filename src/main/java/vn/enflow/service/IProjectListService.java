package vn.enflow.service;

import vn.enflow.dto.request.ProjectListCreatetionRequest;
import vn.enflow.dto.request.ProjectListUpdateRequest;
import vn.enflow.dto.respone.ProjectListResponse;

import java.util.List;

public interface IProjectListService {


    ProjectListResponse createtionProjectList(Long projectId, ProjectListCreatetionRequest listRequest);
    ProjectListResponse getProjectListById(Long listId);
    List<ProjectListResponse> getProjectListsByProjectId(Long projectId);
    ProjectListResponse updateProjectList(Long listId, ProjectListUpdateRequest updateRequest);
    void deleteProjectList(Long listId);
}
