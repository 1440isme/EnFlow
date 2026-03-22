package vn.enflow.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.enflow.dto.request.ProjectCreatetionRequest;
import vn.enflow.dto.respone.ProjectResponse;
import vn.enflow.service.IProjectService;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ProjectController {

    private final IProjectService projectService;

    public ProjectController(IProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping(path = "/workspaces/{workspaceId}/projects", consumes = "application/json", produces = "application/json")
    public ResponseEntity<ProjectResponse> createProject(@PathVariable("workspaceId") Long workspaceId,
                                                         @RequestBody ProjectCreatetionRequest request) {
        ProjectResponse created = projectService.createtionProject(workspaceId, request);
        return ResponseEntity.status(201).body(created);
    }

    @GetMapping(path = "/projects/{projectId}", produces = "application/json")
    public ResponseEntity<ProjectResponse> getProjectById(@PathVariable("projectId") Long projectId) {
        ProjectResponse resp = projectService.getProjectById(projectId);
        if (resp == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(resp);
    }

    @GetMapping(path = "/workspaces/{workspaceId}/projects", produces = "application/json")
    public ResponseEntity<List<ProjectResponse>> getProjectsByWorkspace(@PathVariable("workspaceId") Long workspaceId) {
        List<ProjectResponse> list = projectService.getProjectsByWorkspaceId(workspaceId);
        return ResponseEntity.ok(list);
    }

    @PutMapping(path = "/projects/{projectId}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<ProjectResponse> updateProject(@PathVariable("projectId") Long projectId,
                                                         @RequestBody ProjectCreatetionRequest request) {
        ProjectResponse updated = projectService.updateProject(projectId, request);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping(path = "/projects/{projectId}")
    public ResponseEntity<Void> deleteProject(@PathVariable("projectId") Long projectId) {
        projectService.deleteProject(projectId);
        return ResponseEntity.noContent().build();
    }
}
