package vn.enflow.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import vn.enflow.dto.request.ActivityLogCreationRequest;
import vn.enflow.dto.request.ActivityLogUpdateRequest;
import vn.enflow.dto.respone.ActivityLogResponse;
import vn.enflow.entity.ActivityLog;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ActivityLogMapper {

    // Ignore fields that are auto-generated or managed by the system
    @Mapping(target = "logId", ignore = true)
    @Mapping(target = "taskId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "workspace", ignore = true)
    @Mapping(target = "project", ignore = true)
    @Mapping(target = "task", ignore = true)
    @Mapping(target = "actor", ignore = true)
    ActivityLog toActivityLog(ActivityLogCreationRequest request);

    // For update, we only want to update certain fields and ignore the rest
    @Mapping(target = "logId", ignore = true)
    @Mapping(target = "workspaceId", ignore = true)
    @Mapping(target = "projectId", ignore = true)
    @Mapping(target = "taskId", ignore = true)
    @Mapping(target = "actorId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "workspace", ignore = true)
    @Mapping(target = "project", ignore = true)
    @Mapping(target = "task", ignore = true)
    @Mapping(target = "actor", ignore = true)
    void updateActivityLog(@MappingTarget ActivityLog activityLog, ActivityLogUpdateRequest request);

    ActivityLogResponse toActivityLogResponse(ActivityLog activityLog);
}
