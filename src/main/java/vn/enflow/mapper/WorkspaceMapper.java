package vn.enflow.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import vn.enflow.dto.request.WorkspaceRequest;
import vn.enflow.dto.request.WorkspaceUpdateRequest;
import vn.enflow.dto.respone.WorkspaceResponse;
import vn.enflow.entity.Workspace;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface WorkspaceMapper {

    // WorkspaceRequest → Workspace
    // owner được set thủ công trong service (cần load từ DB theo ownerUserId)
    @Mapping(target = "workspaceId", ignore = true)
    @Mapping(target = "owner", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "members", ignore = true)
    @Mapping(target = "projects", ignore = true)
    @Mapping(target = "tags", ignore = true)
    @Mapping(target = "activityLogs", ignore = true)
    Workspace toWorkspace(WorkspaceRequest request);

    // Workspace → WorkspaceResponse
    // ownerUserId lấy từ owner.userId (nested field)
    @Mapping(target = "ownerUserId", source = "owner.userId")
    WorkspaceResponse toWorkspaceResponse(Workspace workspace);

    // WorkspaceUpdateRequest → Workspace (chỉ cập nhật name, description, isPrivate)
    @Mapping(target = "workspaceId", ignore = true)
    @Mapping(target = "workspaceKey", ignore = true)
    @Mapping(target = "owner", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "members", ignore = true)
    @Mapping(target = "projects", ignore = true)
    @Mapping(target = "tags", ignore = true)
    @Mapping(target = "activityLogs", ignore = true)
    void updateWorkspace(@MappingTarget Workspace workspace, WorkspaceUpdateRequest request);
}
