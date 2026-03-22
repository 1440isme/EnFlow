package vn.enflow.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.enflow.dto.request.AttachmentCreationRequest;
import vn.enflow.dto.request.AttachmentUpdateRequest;
import vn.enflow.dto.respone.AttachmentResponse;
import vn.enflow.service.IAttachmentService;

import java.util.List;

@RestController
@RequestMapping("/attachments")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AttachmentController {

    IAttachmentService attachmentService;

    @PostMapping("/tasks/{taskId}")
    ResponseEntity<AttachmentResponse> createAttachment(@PathVariable Long taskId,
                                                        @RequestBody AttachmentCreationRequest request) {
        return ResponseEntity.status(201).body(attachmentService.createAttachment(taskId, request));
    }

    @GetMapping("/{attachmentId}")
    ResponseEntity<AttachmentResponse> getAttachmentById(@PathVariable Long attachmentId) {
        return ResponseEntity.ok(attachmentService.getAttachmentById(attachmentId));
    }

    @GetMapping("/tasks/{taskId}")
    ResponseEntity<List<AttachmentResponse>> getAttachmentsByTaskId(@PathVariable Long taskId) {
        return ResponseEntity.ok(attachmentService.getAttachmentsByTaskId(taskId));
    }

    @PutMapping("/{attachmentId}")
    ResponseEntity<AttachmentResponse> updateAttachment(@PathVariable Long attachmentId,
                                                        @RequestBody AttachmentUpdateRequest request) {
        return ResponseEntity.ok(attachmentService.updateAttachment(attachmentId, request));
    }

    @DeleteMapping("/{attachmentId}")
    ResponseEntity<Void> deleteAttachment(@PathVariable Long attachmentId) {
        attachmentService.deleteAttachment(attachmentId);
        return ResponseEntity.noContent().build();
    }
}
