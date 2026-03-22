package vn.enflow.mapper;

import org.mapstruct.*;
import vn.enflow.dto.request.ProjectCreatetionRequest;
import vn.enflow.dto.respone.ProjectResponse;
import vn.enflow.entity.Project;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ProjectMapper {

    // ProjectCreatetionRequest → Project
    @Mapping(target = "projectId", ignore = true)
    @Mapping(target = "workspace", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "lists", ignore = true)
    @Mapping(target = "statuses", ignore = true)
    @Mapping(target = "tasks", ignore = true)
    @Mapping(target = "activityLogs", ignore = true)
    @Mapping(target = "archived", source = "archive")
    Project toProject(ProjectCreatetionRequest request);

    // Project → ProjectResponse
    @Mapping(target = "idProject", source = "projectId")
    @Mapping(target = "workspaceId", source = "workspace.workspaceId")
    ProjectResponse toProjectResponse(Project project);

    // ProjectCreatetionRequest → Project (update)
    @Mapping(target = "projectId", ignore = true)
    @Mapping(target = "workspace", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "lists", ignore = true)
    @Mapping(target = "statuses", ignore = true)
    @Mapping(target = "tasks", ignore = true)
    @Mapping(target = "activityLogs", ignore = true)
    @Mapping(target = "archived", source = "archive")
    void updateProject(@MappingTarget Project project, ProjectCreatetionRequest request);
}
