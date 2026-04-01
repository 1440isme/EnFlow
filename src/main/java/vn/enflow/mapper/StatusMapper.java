package vn.enflow.mapper;

import org.mapstruct.*;
import vn.enflow.dto.request.StatusesCreatetionRequest;
import vn.enflow.dto.request.StatusesUpdateRequest;
import vn.enflow.dto.respone.StatusesRespone;
import vn.enflow.entity.Status;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, imports = {
        Status.StatusGroup.class })
public interface StatusMapper {

    // StatusesCreatetionRequest → Status
    @Mapping(target = "statusId", ignore = true)
    @Mapping(target = "project", ignore = true)
    @Mapping(target = "list", ignore = true)
    @Mapping(target = "tasks", ignore = true)
    @Mapping(target = "statusGroup", expression = "java(StatusGroup.fromString(request.getStatusGroup()))")
    Status toStatus(StatusesCreatetionRequest request);

    // Status → StatusesRespone
    @Mapping(target = "statusGroup", expression = "java(status.getStatusGroup().name())")
    @Mapping(target = "position", source = "statusGroup.order")
    @Mapping(target = "listId", source = "list.listId")
    @Mapping(target = "projectId", source = "project.projectId")
    StatusesRespone toStatusesRespone(Status status);

    // StatusesUpdateRequest → Status (update)
    @Mapping(target = "statusId", ignore = true)
    @Mapping(target = "project", ignore = true)
    @Mapping(target = "list", ignore = true)
    @Mapping(target = "tasks", ignore = true)
    @Mapping(target = "statusGroup", expression = "java(request.getStatusGroup() != null ? StatusGroup.fromString(request.getStatusGroup()) : status.getStatusGroup())")
    void updateStatus(@MappingTarget Status status, StatusesUpdateRequest request);
}
