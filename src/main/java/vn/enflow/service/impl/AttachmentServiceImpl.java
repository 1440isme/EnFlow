package vn.enflow.service.impl;

import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import vn.enflow.dto.request.AttachmentCreationRequest;
import vn.enflow.dto.request.AttachmentUpdateRequest;
import vn.enflow.dto.respone.AttachmentResponse;
import vn.enflow.entity.Attachment;
import vn.enflow.entity.Task;
import vn.enflow.entity.User;
import vn.enflow.mapper.AttachmentMapper;
import vn.enflow.repository.AttachmentRepository;
import vn.enflow.repository.TaskRepository;
import vn.enflow.repository.UserRepository;
import vn.enflow.security.SecurityUtils;
import vn.enflow.service.IAttachmentService;
import vn.enflow.service.WorkspaceAccessService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AttachmentServiceImpl implements IAttachmentService {

    AttachmentRepository attachmentRepository;
    TaskRepository taskRepository;
    UserRepository userRepository;
    AttachmentMapper attachmentMapper;
    WorkspaceAccessService workspaceAccessService;

    @Override
    @Transactional
    public AttachmentResponse createAttachment(Long taskId, AttachmentCreationRequest request) {
        if (request.getUploadedBy() == null) {
            throw new RuntimeException("uploadedBy là bắt buộc");
        }

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy task với id: " + taskId));
        workspaceAccessService.requireWriteAccess(task.getProject().getWorkspace().getWorkspaceId(), SecurityUtils.currentUserId());

        User uploader = userRepository.findById(request.getUploadedBy())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy user với id: " + request.getUploadedBy()));

        Attachment attachment = attachmentMapper.toAttachment(request);
        attachment.setTaskId(task.getTaskId());
        attachment.setUploadedBy(uploader.getUserId());
        attachment.setCreatedAt(LocalDateTime.now());

        Attachment saved = attachmentRepository.save(attachment);
        return attachmentMapper.toAttachmentResponse(saved);
    }

    @Override
    public AttachmentResponse getAttachmentById(Long attachmentId) {
        Attachment attachment = attachmentRepository.findById(attachmentId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy attachment với id: " + attachmentId));
        Task task = taskRepository.findById(attachment.getTaskId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy task với id: " + attachment.getTaskId()));
        workspaceAccessService.requireActiveMembership(task.getProject().getWorkspace().getWorkspaceId(), SecurityUtils.currentUserId());
        return attachmentMapper.toAttachmentResponse(attachment);
    }

    @Override
    public List<AttachmentResponse> getAttachmentsByTaskId(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy task với id: " + taskId));
        workspaceAccessService.requireActiveMembership(task.getProject().getWorkspace().getWorkspaceId(), SecurityUtils.currentUserId());
        return attachmentRepository.findByTaskId(taskId).stream()
                .map(attachmentMapper::toAttachmentResponse)
                .toList();
    }

    @Override
    @Transactional
    public AttachmentResponse updateAttachment(Long attachmentId, AttachmentUpdateRequest request) {
        Attachment attachment = attachmentRepository.findById(attachmentId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy attachment với id: " + attachmentId));
        Task task = taskRepository.findById(attachment.getTaskId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy task với id: " + attachment.getTaskId()));
        workspaceAccessService.requireWriteAccess(task.getProject().getWorkspace().getWorkspaceId(), SecurityUtils.currentUserId());

        attachmentMapper.updateAttachment(attachment, request);

        Attachment saved = attachmentRepository.save(attachment);
        return attachmentMapper.toAttachmentResponse(saved);
    }

    @Override
    @Transactional
    public void deleteAttachment(Long attachmentId) {
        Attachment attachment = attachmentRepository.findById(attachmentId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy attachment với id: " + attachmentId));
        Task task = taskRepository.findById(attachment.getTaskId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy task với id: " + attachment.getTaskId()));
        workspaceAccessService.requireWriteAccess(task.getProject().getWorkspace().getWorkspaceId(), SecurityUtils.currentUserId());
        attachmentRepository.deleteById(attachmentId);
    }
}
