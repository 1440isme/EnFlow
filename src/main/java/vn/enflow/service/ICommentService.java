package vn.enflow.service;

import java.util.List;

import vn.enflow.dto.respone.CommentResponse;
import vn.enflow.entity.Comment;

public interface ICommentService {
    CommentResponse createComment(Long taskId, Comment comment);

    CommentResponse getCommentById(Long commentId);
    
    List<CommentResponse> getCommentsByTaskId(Long taskId);

    CommentResponse updateComment(Long commentId, Comment comment);

    void deleteComment(Long commentId);
}
