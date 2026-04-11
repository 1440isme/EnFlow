package vn.enflow.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.enflow.dto.request.ProjectListCreatetionRequest;
import vn.enflow.dto.request.ProjectListUpdateRequest;
import vn.enflow.dto.respone.ProjectListResponse;
import vn.enflow.service.IProjectListService;

import java.util.List;

@RestController
@RequestMapping("/lists")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProjectListController {

    IProjectListService projectListService;

    @PostMapping("/projects/{projectId}")
    ResponseEntity<ProjectListResponse> createProjectList(@PathVariable Long projectId,
            @RequestBody ProjectListCreatetionRequest request) {
        return ResponseEntity.status(201).body(projectListService.createtionProjectList(projectId, request));
    }

    @GetMapping("/{listId}")
    ResponseEntity<ProjectListResponse> getProjectListById(@PathVariable Long listId) {
        return ResponseEntity.ok(projectListService.getProjectListById(listId));
    }

    @GetMapping("/projects/{projectId}")
    ResponseEntity<List<ProjectListResponse>> getListsByProject(@PathVariable Long projectId) {
        return ResponseEntity.ok(projectListService.getProjectListsByProjectId(projectId));
    }

    @PutMapping("/{listId}")
    ResponseEntity<ProjectListResponse> updateProjectList(@PathVariable Long listId,
            @RequestBody ProjectListUpdateRequest request) {
        return ResponseEntity.ok(projectListService.updateProjectList(listId, request));
    }

    @DeleteMapping("/{listId}")
    ResponseEntity<Void> deleteProjectList(@PathVariable Long listId) {
        projectListService.deleteProjectList(listId);
        return ResponseEntity.noContent().build();
    }
}
