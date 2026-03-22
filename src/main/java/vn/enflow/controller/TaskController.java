package vn.enflow.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.enflow.dto.request.TaskCreatetionRequest;
import vn.enflow.dto.request.TaskUpdateRequest;
import vn.enflow.dto.respone.TaskResponse;
import vn.enflow.service.ITaskService;

import java.util.List;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TaskController {

    ITaskService taskService;

    @PostMapping("/projects/{projectId}/lists/{listId}/statuses/{statusId}")
    ResponseEntity<TaskResponse> createTask(@PathVariable Long projectId,
                                            @PathVariable Long listId,
                                            @PathVariable Long statusId,
                                            @RequestBody TaskCreatetionRequest request) {
        return ResponseEntity.status(201).body(taskService.createtionTask(projectId, listId, statusId, request));
    }

    @GetMapping("/{taskId}")
    ResponseEntity<TaskResponse> getTaskById(@PathVariable Long taskId) {
        return ResponseEntity.ok(taskService.getTaskById(taskId));
    }

    @GetMapping("/projects/{projectId}")
    ResponseEntity<List<TaskResponse>> getTasksByProject(@PathVariable Long projectId) {
        return ResponseEntity.ok(taskService.getTasksByProjectId(projectId));
    }

    @GetMapping("/lists/{listId}")
    ResponseEntity<List<TaskResponse>> getTasksByList(@PathVariable Long listId) {
        return ResponseEntity.ok(taskService.getTasksByListId(listId));
    }

    @GetMapping("/statuses/{statusId}")
    ResponseEntity<List<TaskResponse>> getTasksByStatus(@PathVariable Long statusId) {
        return ResponseEntity.ok(taskService.getTasksByStatusId(statusId));
    }

    @PutMapping("/{taskId}")
    ResponseEntity<TaskResponse> updateTask(@PathVariable Long taskId,
                                            @RequestBody TaskUpdateRequest request) {
        return ResponseEntity.ok(taskService.updateTask(taskId, request));
    }

    @DeleteMapping("/{taskId}")
    ResponseEntity<Void> deleteTask(@PathVariable Long taskId) {
        taskService.deleteTask(taskId);
        return ResponseEntity.noContent().build();
    }
}
