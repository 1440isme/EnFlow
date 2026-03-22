package vn.enflow.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import vn.enflow.dto.request.AttachmentCreationRequest;
import vn.enflow.dto.request.AttachmentUpdateRequest;
import vn.enflow.dto.respone.AttachmentResponse;
import vn.enflow.entity.Attachment;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface AttachmentMapper {

    // Ignore fields that are auto-generated or managed by the system
    @Mapping(target = "attachmentId", ignore = true)
    @Mapping(target = "taskId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "task", ignore = true)
    @Mapping(target = "uploader", ignore = true)
    Attachment toAttachment(AttachmentCreationRequest request);

    // For update, we only want to update certain fields and ignore the rest
    @Mapping(target = "attachmentId", ignore = true)
    @Mapping(target = "taskId", ignore = true)
    @Mapping(target = "uploadedBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "task", ignore = true)
    @Mapping(target = "uploader", ignore = true)
    void updateAttachment(@MappingTarget Attachment attachment, AttachmentUpdateRequest request);

    AttachmentResponse toAttachmentResponse(Attachment attachment);
}
