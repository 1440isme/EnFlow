package vn.enflow.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.enflow.dto.request.TaskTagRequest;
import vn.enflow.dto.respone.TaskTagResponse;
import vn.enflow.service.ITaskTagService;

import java.util.List;

@RestController
@RequestMapping("/task-tags")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TaskTagController {

    ITaskTagService taskTagService;

    @PostMapping("/tasks/{taskId}")
    ResponseEntity<TaskTagResponse> addTagToTask(@PathVariable Long taskId,
                                                 @RequestBody TaskTagRequest request) {
        return ResponseEntity.status(201).body(taskTagService.addTagToTask(taskId, request));
    }

    @GetMapping("/tasks/{taskId}")
    ResponseEntity<List<TaskTagResponse>> getTagsByTask(@PathVariable Long taskId) {
        return ResponseEntity.ok(taskTagService.getTagsByTaskId(taskId));
    }

    @GetMapping("/tags/{tagId}")
    ResponseEntity<List<TaskTagResponse>> getTasksByTag(@PathVariable Long tagId) {
        return ResponseEntity.ok(taskTagService.getTasksByTagId(tagId));
    }

    @DeleteMapping("/tasks/{taskId}/tags/{tagId}")
    ResponseEntity<Void> removeTagFromTask(@PathVariable Long taskId,
                                           @PathVariable Long tagId) {
        taskTagService.removeTagFromTask(taskId, tagId);
        return ResponseEntity.noContent().build();
    }
}
