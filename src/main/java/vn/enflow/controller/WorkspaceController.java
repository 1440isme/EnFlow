package vn.enflow.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.enflow.dto.request.WorkspaceMemberRequest;
import vn.enflow.dto.request.WorkspaceMemberUpdateRequest;
import vn.enflow.dto.request.WorkspaceRequest;
import vn.enflow.dto.request.WorkspaceUpdateRequest;
import vn.enflow.dto.respone.WorkspaceMemberResponse;
import vn.enflow.dto.respone.WorkspaceResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import vn.enflow.security.SecurityUtils;
import vn.enflow.service.IWorkspaceMemberService;
import vn.enflow.service.IWorkspaceService;

import java.util.List;

@RestController
@RequestMapping("/workspaces")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WorkspaceController {

    IWorkspaceService workspaceService;
    IWorkspaceMemberService workspaceMemberService;

    // ── Workspace CRUD ────────────────────────────────────────────────────────

    @PostMapping
    ResponseEntity<WorkspaceResponse> create(@RequestBody WorkspaceRequest request) {
        return ResponseEntity.ok(workspaceService.create(request));
    }

    @GetMapping
    ResponseEntity<List<WorkspaceResponse>> listAccessible() {
        return ResponseEntity.ok(workspaceService.findAccessibleForCurrentUser());
    }

    @GetMapping("/{workspaceId}")
    ResponseEntity<WorkspaceResponse> findById(@PathVariable Long workspaceId) {
        return ResponseEntity.ok(workspaceService.findById(workspaceId));
    }

    @GetMapping("/owner/{ownerUserId}")
    ResponseEntity<List<WorkspaceResponse>> findByOwner(@PathVariable Long ownerUserId) {
        if (!SecurityUtils.currentUserId().equals(ownerUserId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Chỉ xem được workspace bạn sở hữu");
        }
        return ResponseEntity.ok(workspaceService.findByOwner(ownerUserId));
    }

    @PutMapping("/{workspaceId}")
    ResponseEntity<WorkspaceResponse> update(@PathVariable Long workspaceId,
                                             @RequestBody WorkspaceUpdateRequest request) {
        return ResponseEntity.ok(workspaceService.update(workspaceId, request));
    }

    @DeleteMapping("/{workspaceId}")
    ResponseEntity<Void> delete(@PathVariable Long workspaceId) {
        workspaceService.delete(workspaceId);
        return ResponseEntity.noContent().build();
    }

    // ── WorkspaceMember ───────────────────────────────────────────────────────

    @PostMapping("/{workspaceId}/members")
    ResponseEntity<WorkspaceMemberResponse> addMember(@PathVariable Long workspaceId,
                                                      @RequestBody WorkspaceMemberRequest request) {
        return ResponseEntity.ok(workspaceMemberService.addMember(workspaceId, request));
    }

    @GetMapping("/{workspaceId}/members")
    ResponseEntity<List<WorkspaceMemberResponse>> getMembers(@PathVariable Long workspaceId) {
        return ResponseEntity.ok(workspaceMemberService.getMembersByWorkspace(workspaceId));
    }

    @PutMapping("/{workspaceId}/members/{userId}")
    ResponseEntity<WorkspaceMemberResponse> updateMember(@PathVariable Long workspaceId,
                                                         @PathVariable Long userId,
                                                         @RequestBody WorkspaceMemberUpdateRequest request) {
        return ResponseEntity.ok(workspaceMemberService.updateMember(workspaceId, userId, request));
    }

    @DeleteMapping("/{workspaceId}/members/{userId}")
    ResponseEntity<Void> removeMember(@PathVariable Long workspaceId,
                                      @PathVariable Long userId) {
        workspaceMemberService.removeMember(workspaceId, userId);
        return ResponseEntity.noContent().build();
    }
}
