package vn.enflow.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.enflow.dto.request.StatusesCreatetionRequest;
import vn.enflow.dto.request.StatusesUpdateRequest;
import vn.enflow.dto.respone.StatusesRespone;
import vn.enflow.service.IStatusesService;

import java.util.List;

@RestController
@RequestMapping("/api")
public class StatusesController {

    private final IStatusesService statusesService;

    public StatusesController(IStatusesService statusesService) {
        this.statusesService = statusesService;
    }

    @PostMapping(path = "/projects/{projectId}/lists/{listId}/statuses", consumes = "application/json", produces = "application/json")
    public ResponseEntity<StatusesRespone> createStatus(@PathVariable("projectId") Long projectId,
                                                        @PathVariable("listId") Long listId,
                                                        @RequestBody StatusesCreatetionRequest request) {
        StatusesRespone created = statusesService.createtionStatus(projectId, listId, request);
        return ResponseEntity.status(201).body(created);
    }

    @GetMapping(path = "/statuses/{statusId}", produces = "application/json")
    public ResponseEntity<StatusesRespone> getStatusById(@PathVariable("statusId") Long statusId) {
        StatusesRespone resp = statusesService.getStatusById(statusId);
        if (resp == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(resp);
    }

    @GetMapping(path = "/projects/{projectId}/statuses", produces = "application/json")
    public ResponseEntity<List<StatusesRespone>> getStatusesByProject(@PathVariable("projectId") Long projectId) {
        List<StatusesRespone> list = statusesService.getStatusesByProjectId(projectId);
        return ResponseEntity.ok(list);
    }

    @GetMapping(path = "/lists/{listId}/statuses", produces = "application/json")
    public ResponseEntity<List<StatusesRespone>> getStatusesByList(@PathVariable("listId") Long listId) {
        List<StatusesRespone> list = statusesService.getStatusesByListId(listId);
        return ResponseEntity.ok(list);
    }

    @PutMapping(path = "/statuses/{statusId}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<StatusesRespone> updateStatus(@PathVariable("statusId") Long statusId,
                                                         @RequestBody StatusesUpdateRequest request) {
        StatusesRespone updated = statusesService.updateStatus(statusId, request);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping(path = "/statuses/{statusId}")
    public ResponseEntity<Void> deleteStatus(@PathVariable("statusId") Long statusId) {
        statusesService.deleteStatus(statusId);
        return ResponseEntity.noContent().build();
    }
}
