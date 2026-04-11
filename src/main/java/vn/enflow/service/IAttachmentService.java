package vn.enflow.service;

import vn.enflow.dto.request.AttachmentCreationRequest;
import vn.enflow.dto.request.AttachmentUpdateRequest;
import vn.enflow.dto.respone.AttachmentResponse;

import java.util.List;

public interface IAttachmentService {
    AttachmentResponse createAttachment(Long taskId, AttachmentCreationRequest request);
    
    AttachmentResponse getAttachmentById(Long attachmentId);
    
    List<AttachmentResponse> getAttachmentsByTaskId(Long taskId);
    
    AttachmentResponse updateAttachment(Long attachmentId, AttachmentUpdateRequest request);
    
    void deleteAttachment(Long attachmentId);
}
