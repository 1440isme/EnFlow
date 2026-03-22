package vn.enflow.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.enflow.dto.request.TagCreatetionRequest;
import vn.enflow.dto.request.TagUpdateRequest;
import vn.enflow.dto.respone.TagResponse;
import vn.enflow.service.ITagService;

import java.util.List;

@RestController
@RequestMapping("/tags")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TagController {

    ITagService tagService;

    @PostMapping("/workspaces/{workspaceId}")
    ResponseEntity<TagResponse> createTag(@PathVariable Long workspaceId,
                                          @RequestBody TagCreatetionRequest request) {
        return ResponseEntity.status(201).body(tagService.createtionTag(workspaceId, request));
    }

    @GetMapping("/{tagId}")
    ResponseEntity<TagResponse> getTagById(@PathVariable Long tagId) {
        return ResponseEntity.ok(tagService.getTagById(tagId));
    }

    @GetMapping("/workspaces/{workspaceId}")
    ResponseEntity<List<TagResponse>> getTagsByWorkspace(@PathVariable Long workspaceId) {
        return ResponseEntity.ok(tagService.getTagsByWorkspaceId(workspaceId));
    }

    @PutMapping("/{tagId}")
    ResponseEntity<TagResponse> updateTag(@PathVariable Long tagId,
                                          @RequestBody TagUpdateRequest request) {
        return ResponseEntity.ok(tagService.updateTag(tagId, request));
    }

    @DeleteMapping("/{tagId}")
    ResponseEntity<Void> deleteTag(@PathVariable Long tagId) {
        tagService.deleteTag(tagId);
        return ResponseEntity.noContent().build();
    }
}
