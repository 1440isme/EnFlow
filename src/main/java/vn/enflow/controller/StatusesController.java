package vn.enflow.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.enflow.dto.request.StatusesCreatetionRequest;
import vn.enflow.dto.request.StatusesUpdateRequest;
import vn.enflow.dto.respone.StatusesRespone;
import vn.enflow.service.IStatusesService;

import java.util.List;

@RestController
@RequestMapping("/statuses")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StatusesController {

    IStatusesService statusesService;

    @PostMapping("/projects/{projectId}/lists/{listId}")
    ResponseEntity<StatusesRespone> createStatus(@PathVariable Long projectId,
            @PathVariable Long listId,
            @RequestBody StatusesCreatetionRequest request) {
        return ResponseEntity.status(201).body(statusesService.createtionStatus(projectId, listId, request));
    }

    @GetMapping("/{statusId}")
    ResponseEntity<StatusesRespone> getStatusById(@PathVariable Long statusId) {
        return ResponseEntity.ok(statusesService.getStatusById(statusId));
    }

    @GetMapping("/projects/{projectId}")
    ResponseEntity<List<StatusesRespone>> getStatusesByProject(@PathVariable Long projectId) {
        return ResponseEntity.ok(statusesService.getStatusesByProjectId(projectId));
    }

    @GetMapping("/lists/{listId}")
    ResponseEntity<List<StatusesRespone>> getStatusesByList(@PathVariable Long listId) {
        return ResponseEntity.ok(statusesService.getStatusesByListId(listId));
    }

    @PutMapping("/{statusId}")
    ResponseEntity<StatusesRespone> updateStatus(@PathVariable Long statusId,
            @RequestBody StatusesUpdateRequest request) {
        return ResponseEntity.ok(statusesService.updateStatus(statusId, request));
    }

    @DeleteMapping("/{statusId}")
    ResponseEntity<Void> deleteStatus(@PathVariable Long statusId) {
        statusesService.deleteStatus(statusId);
        return ResponseEntity.noContent().build();
    }
}
