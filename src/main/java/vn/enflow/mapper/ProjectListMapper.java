package vn.enflow.mapper;

import org.mapstruct.*;
import vn.enflow.dto.request.ProjectListCreatetionRequest;
import vn.enflow.dto.request.ProjectListUpdateRequest;
import vn.enflow.dto.respone.ProjectListResponse;
import vn.enflow.entity.ProjectList;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ProjectListMapper {

    // ProjectListCreatetionRequest → ProjectList
    @Mapping(target = "listId", ignore = true)
    @Mapping(target = "project", ignore = true)
    @Mapping(target = "statuses", ignore = true)
    @Mapping(target = "tasks", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    ProjectList toProjectList(ProjectListCreatetionRequest request);

    // ProjectList → ProjectListResponse
    @Mapping(target = "listProjectId", source = "listId")
    @Mapping(target = "projectId", source = "project.projectId")
    ProjectListResponse toProjectListResponse(ProjectList projectList);

    // ProjectListUpdateRequest → ProjectList (update)
    @Mapping(target = "listId", ignore = true)
    @Mapping(target = "project", ignore = true)
    @Mapping(target = "statuses", ignore = true)
    @Mapping(target = "tasks", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateProjectList(@MappingTarget ProjectList projectList, ProjectListUpdateRequest request);
}
