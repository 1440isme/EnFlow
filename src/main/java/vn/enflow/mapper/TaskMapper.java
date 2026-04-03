package vn.enflow.mapper;

import org.mapstruct.*;
import vn.enflow.dto.request.TaskCreatetionRequest;
import vn.enflow.dto.request.TaskUpdateRequest;
import vn.enflow.dto.respone.TaskResponse;
import vn.enflow.entity.Task;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        imports = {Task.TaskType.class, Task.Priority.class})
public interface TaskMapper {

    @Mapping(target = "taskId", ignore = true)
    @Mapping(target = "project", ignore = true)
    @Mapping(target = "list", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "parentTask", ignore = true)
    @Mapping(target = "reporter", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "subtasks", ignore = true)
    @Mapping(target = "assignees", ignore = true)
    @Mapping(target = "taskTags", ignore = true)
    @Mapping(target = "comments", ignore = true)
    @Mapping(target = "attachments", ignore = true)
    @Mapping(target = "activityLogs", ignore = true)
    @Mapping(target = "taskType", expression = "java(request.getTaskType() != null ? TaskType.valueOf(request.getTaskType()) : TaskType.task)")
    @Mapping(target = "priority", expression = "java(request.getPriority() != null ? Priority.valueOf(request.getPriority()) : Priority.normal)")
    Task toTask(TaskCreatetionRequest request);

    @Mapping(target = "projectId", source = "project.projectId")
    @Mapping(target = "projectName", source = "project.name")
    @Mapping(target = "listId", source = "list.listId")
    @Mapping(target = "listName", source = "list.name")
    @Mapping(target = "statusId", source = "status.statusId")
    @Mapping(target = "parentTaskId", source = "parentTask.taskId")
    @Mapping(target = "reporterId", source = "reporter.userId")
    @Mapping(target = "taskType", expression = "java(task.getTaskType() != null ? task.getTaskType().name() : null)")
    @Mapping(target = "priority", expression = "java(task.getPriority() != null ? task.getPriority().name() : null)")
    TaskResponse toTaskResponse(Task task);

    @Mapping(target = "taskId", ignore = true)
    @Mapping(target = "project", ignore = true)
    @Mapping(target = "list", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "parentTask", ignore = true)
    @Mapping(target = "reporter", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "subtasks", ignore = true)
    @Mapping(target = "assignees", ignore = true)
    @Mapping(target = "taskTags", ignore = true)
    @Mapping(target = "comments", ignore = true)
    @Mapping(target = "attachments", ignore = true)
    @Mapping(target = "activityLogs", ignore = true)
    @Mapping(target = "taskType", expression = "java(request.getTaskType() != null ? TaskType.valueOf(request.getTaskType()) : task.getTaskType())")
    @Mapping(target = "priority", expression = "java(request.getPriority() != null ? Priority.valueOf(request.getPriority()) : task.getPriority())")
    void updateTask(@MappingTarget Task task, TaskUpdateRequest request);
}
