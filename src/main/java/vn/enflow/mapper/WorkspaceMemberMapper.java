package vn.enflow.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import vn.enflow.dto.respone.WorkspaceMemberResponse;
import vn.enflow.entity.WorkspaceMember;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface WorkspaceMemberMapper {

    // WorkspaceMember → WorkspaceMemberResponse
    // workspaceId và userId lấy từ composite key (id)
    @Mapping(target = "workspaceId", source = "id.workspaceId")
    @Mapping(target = "userId", source = "id.userId")
    WorkspaceMemberResponse toWorkspaceMemberResponse(WorkspaceMember member);
}
