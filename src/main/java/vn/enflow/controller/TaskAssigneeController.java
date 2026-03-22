package vn.enflow.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.enflow.dto.request.TaskAssigneeRequest;
import vn.enflow.dto.request.TaskAssigneeUpdateRequest;
import vn.enflow.dto.respone.TaskAssigneeResponse;
import vn.enflow.service.ITaskAssigneeService;

import java.util.List;

@RestController
@RequestMapping("/task-assignees")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TaskAssigneeController {

    ITaskAssigneeService taskAssigneeService;

    @PostMapping("/tasks/{taskId}")
    ResponseEntity<TaskAssigneeResponse> addAssignee(@PathVariable Long taskId,
                                                     @RequestBody TaskAssigneeRequest request) {
        return ResponseEntity.status(201).body(taskAssigneeService.addAssignee(taskId, request));
    }

    @GetMapping("/tasks/{taskId}")
    ResponseEntity<List<TaskAssigneeResponse>> getAssigneesByTask(@PathVariable Long taskId) {
        return ResponseEntity.ok(taskAssigneeService.getAssigneesByTaskId(taskId));
    }

    @GetMapping("/users/{userId}")
    ResponseEntity<List<TaskAssigneeResponse>> getAssignmentsByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(taskAssigneeService.getAssignmentsByUserId(userId));
    }

    @PutMapping("/tasks/{taskId}/users/{userId}")
    ResponseEntity<TaskAssigneeResponse> updateAssignee(@PathVariable Long taskId,
                                                        @PathVariable Long userId,
                                                        @RequestBody TaskAssigneeUpdateRequest request) {
        return ResponseEntity.ok(taskAssigneeService.updateAssignee(taskId, userId, request));
    }

    @DeleteMapping("/tasks/{taskId}/users/{userId}")
    ResponseEntity<Void> removeAssignee(@PathVariable Long taskId,
                                        @PathVariable Long userId) {
        taskAssigneeService.removeAssignee(taskId, userId);
        return ResponseEntity.noContent().build();
    }
}
