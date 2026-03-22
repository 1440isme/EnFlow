package vn.enflow.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.enflow.dto.request.ActivityLogCreationRequest;
import vn.enflow.dto.request.ActivityLogUpdateRequest;
import vn.enflow.dto.respone.ActivityLogResponse;
import vn.enflow.service.IActivityLogService;

import java.util.List;

@RestController
@RequestMapping("/activity-logs")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ActivityLogController {

    IActivityLogService activityLogService;

    @PostMapping("/tasks/{taskId}")
    ResponseEntity<ActivityLogResponse> createActivityLog(@PathVariable Long taskId,
                                                          @RequestBody ActivityLogCreationRequest request) {
        return ResponseEntity.status(201).body(activityLogService.createActivityLog(taskId, request));
    }

    @GetMapping("/{logId}")
    ResponseEntity<ActivityLogResponse> getActivityLogById(@PathVariable Long logId) {
        return ResponseEntity.ok(activityLogService.getActivityLogById(logId));
    }

    @GetMapping("/workspaces/{workspaceId}")
    ResponseEntity<List<ActivityLogResponse>> getActivityLogsByWorkspaceId(@PathVariable Long workspaceId) {
        return ResponseEntity.ok(activityLogService.getActivityLogsByWorkspaceId(workspaceId));
    }

    @GetMapping("/projects/{projectId}")
    ResponseEntity<List<ActivityLogResponse>> getActivityLogsByProjectId(@PathVariable Long projectId) {
        return ResponseEntity.ok(activityLogService.getActivityLogsByProjectId(projectId));
    }

    @GetMapping("/tasks/{taskId}")
    ResponseEntity<List<ActivityLogResponse>> getActivityLogsByTaskId(@PathVariable Long taskId) {
        return ResponseEntity.ok(activityLogService.getActivityLogsByTaskId(taskId));
    }

    @PutMapping("/{logId}")
    ResponseEntity<ActivityLogResponse> updateActivityLog(@PathVariable Long logId,
                                                          @RequestBody ActivityLogUpdateRequest request) {
        return ResponseEntity.ok(activityLogService.updateActivityLog(logId, request));
    }

    @DeleteMapping("/{logId}")
    ResponseEntity<Void> deleteActivityLog(@PathVariable Long logId) {
        activityLogService.deleteActivityLog(logId);
        return ResponseEntity.noContent().build();
    }
}
