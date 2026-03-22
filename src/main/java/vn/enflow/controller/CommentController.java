package vn.enflow.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.enflow.dto.respone.CommentResponse;
import vn.enflow.entity.Comment;
import vn.enflow.service.ICommentService;

import java.util.List;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CommentController {

    ICommentService commentService;

    @PostMapping("/tasks/{taskId}")
    ResponseEntity<CommentResponse> createComment(@PathVariable Long taskId,
                                                  @RequestBody Comment comment) {
        return ResponseEntity.status(201).body(commentService.createComment(taskId, comment));
    }

    @GetMapping("/{commentId}")
    ResponseEntity<CommentResponse> getCommentById(@PathVariable Long commentId) {
        return ResponseEntity.ok(commentService.getCommentById(commentId));
    }

    @GetMapping("/tasks/{taskId}")
    ResponseEntity<List<CommentResponse>> getCommentsByTaskId(@PathVariable Long taskId) {
        return ResponseEntity.ok(commentService.getCommentsByTaskId(taskId));
    }

    @PutMapping("/{commentId}")
    ResponseEntity<CommentResponse> updateComment(@PathVariable Long commentId,
                                                  @RequestBody Comment comment) {
        return ResponseEntity.ok(commentService.updateComment(commentId, comment));
    }

    @DeleteMapping("/{commentId}")
    ResponseEntity<Void> deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.noContent().build();
    }
}
