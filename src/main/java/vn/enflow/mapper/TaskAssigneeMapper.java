package vn.enflow.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import vn.enflow.dto.respone.TaskAssigneeResponse;
import vn.enflow.entity.TaskAssignee;

@Mapper(componentModel = "spring")
public interface TaskAssigneeMapper {

    @Mapping(target = "taskId", source = "task.taskId")
    @Mapping(target = "userId", source = "user.userId")
    @Mapping(target = "username", source = "user.username")
    @Mapping(target = "fullName", source = "user.fullName")
    @Mapping(target = "email", source = "user.email")
    TaskAssigneeResponse toTaskAssigneeResponse(TaskAssignee taskAssignee);
}
