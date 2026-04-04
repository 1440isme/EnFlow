package vn.enflow.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.enflow.dto.request.ProjectCreatetionRequest;
import vn.enflow.dto.respone.ProjectListStatusesResponse;
import vn.enflow.dto.respone.ProjectResponse;
import vn.enflow.service.IProjectService;

import java.util.List;

@RestController
@RequestMapping("/projects")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProjectController {

    IProjectService projectService;

    @PostMapping("/workspaces/{workspaceId}")
    ResponseEntity<ProjectResponse> createProject(@PathVariable Long workspaceId,
            @RequestBody ProjectCreatetionRequest request) {
        return ResponseEntity.status(201).body(projectService.createtionProject(workspaceId, request));
    }

    @GetMapping("/{projectId}")
    ResponseEntity<ProjectResponse> getProjectById(@PathVariable Long projectId) {
        return ResponseEntity.ok(projectService.getProjectById(projectId));
    }

    @GetMapping("/{projectId}/list-statuses")
    ResponseEntity<ProjectListStatusesResponse> getProjectListStatuses(@PathVariable Long projectId) {
        return ResponseEntity.ok(projectService.getProjectListStatuses(projectId));
    }

    @GetMapping("/workspaces/{workspaceId}")
    ResponseEntity<List<ProjectResponse>> getProjectsByWorkspace(@PathVariable Long workspaceId) {
        return ResponseEntity.ok(projectService.getProjectsByWorkspaceId(workspaceId));
    }

    @PutMapping("/{projectId}")
    ResponseEntity<ProjectResponse> updateProject(@PathVariable Long projectId,
            @RequestBody ProjectCreatetionRequest request) {
        return ResponseEntity.ok(projectService.updateProject(projectId, request));
    }

    @DeleteMapping("/{projectId}")
    ResponseEntity<Void> deleteProject(@PathVariable Long projectId) {
        projectService.deleteProject(projectId);
        return ResponseEntity.noContent().build();
    }
}
