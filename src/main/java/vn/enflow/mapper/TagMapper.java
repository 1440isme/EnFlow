package vn.enflow.mapper;

import org.mapstruct.*;
import vn.enflow.dto.request.TagCreatetionRequest;
import vn.enflow.dto.request.TagUpdateRequest;
import vn.enflow.dto.respone.TagResponse;
import vn.enflow.entity.Tag;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface TagMapper {

    @Mapping(target = "tagId", ignore = true)
    @Mapping(target = "workspace", ignore = true)
    @Mapping(target = "taskTags", ignore = true)
    Tag toTag(TagCreatetionRequest request);

    @Mapping(target = "workspaceId", source = "workspace.workspaceId")
    TagResponse toTagResponse(Tag tag);

    @Mapping(target = "tagId", ignore = true)
    @Mapping(target = "workspace", ignore = true)
    @Mapping(target = "taskTags", ignore = true)
    void updateTag(@MappingTarget Tag tag, TagUpdateRequest request);
}
