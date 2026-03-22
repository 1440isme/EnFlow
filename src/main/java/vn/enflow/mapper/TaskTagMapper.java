package vn.enflow.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import vn.enflow.dto.respone.TaskTagResponse;
import vn.enflow.entity.TaskTag;

@Mapper(componentModel = "spring")
public interface TaskTagMapper {

    @Mapping(target = "taskId", source = "task.taskId")
    @Mapping(target = "tagId", source = "tag.tagId")
    @Mapping(target = "tagName", source = "tag.name")
    @Mapping(target = "tagColor", source = "tag.color")
    TaskTagResponse toTaskTagResponse(TaskTag taskTag);
}
