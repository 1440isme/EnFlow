package vn.enflow.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.enflow.dto.request.ProjectListCreatetionRequest;
import vn.enflow.dto.request.ProjectListUpdateRequest;
import vn.enflow.dto.respone.ProjectListResponse;
import vn.enflow.service.IProjectListService;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ProjectListController {

    private final IProjectListService projectListService;

    public ProjectListController(IProjectListService projectListService) {
        this.projectListService = projectListService;
    }

    @PostMapping(path = "/projects/{projectId}/lists", consumes = "application/json", produces = "application/json")
    public ResponseEntity<ProjectListResponse> createProjectList(@PathVariable("projectId") Long projectId,
                                                                  @RequestBody ProjectListCreatetionRequest request) {
        ProjectListResponse created = projectListService.createtionProjectList(projectId, request);
        return ResponseEntity.status(201).body(created);
    }

    @GetMapping(path = "/lists/{listId}", produces = "application/json")
    public ResponseEntity<ProjectListResponse> getProjectListById(@PathVariable("listId") Long listId) {
        ProjectListResponse resp = projectListService.getProjectListById(listId);
        if (resp == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(resp);
    }

    @GetMapping(path = "/projects/{projectId}/lists", produces = "application/json")
    public ResponseEntity<List<ProjectListResponse>> getListsByProject(@PathVariable("projectId") Long projectId) {
        List<ProjectListResponse> list = projectListService.getProjectListsByProjectId(projectId);
        return ResponseEntity.ok(list);
    }

    @PutMapping(path = "/lists/{listId}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<ProjectListResponse> updateProjectList(@PathVariable("listId") Long listId,
                                                                  @RequestBody ProjectListUpdateRequest request) {
        ProjectListResponse updated = projectListService.updateProjectList(listId, request);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping(path = "/lists/{listId}")
    public ResponseEntity<Void> deleteProjectList(@PathVariable("listId") Long listId) {
        projectListService.deleteProjectList(listId);
        return ResponseEntity.noContent().build();
    }
}
