package vn.enflow.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import jakarta.transaction.Transactional;

import vn.enflow.dto.respone.CommentResponse;
import vn.enflow.entity.Comment;
import vn.enflow.service.ICommentService;
import vn.enflow.repository.CommentRepository;
import vn.enflow.mapper.CommentMapper;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class CommentService implements ICommentService {
    
    CommentRepository commentRepository;
    CommentMapper commentMapper;

    @Override
    @Transactional
    public CommentResponse createComment(Long taskId, Comment comment) {
        comment.setCommentId(null);
        comment.setTaskId(taskId);

        LocalDateTime now = LocalDateTime.now();
        comment.setCreatedAt(now);
        comment.setUpdatedAt(now);

        if (comment.getIsEdited() == null) comment.setIsEdited(false);
        if (comment.getIsDeleted() == null) comment.setIsDeleted(false);

        Comment saved = commentRepository.save(comment);
        return commentMapper.toCommentResponse(saved);
    }

    @Override
    public CommentResponse getCommentById(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy comment với id: " + commentId));

        return commentMapper.toCommentResponse(comment);
    }

    @Override
    public List<CommentResponse> getCommentsByTaskId(Long taskId) {
        return commentRepository.findByTaskId(taskId).stream()
                .map(commentMapper::toCommentResponse)
                .toList();
    }

    @Override
    @Transactional
    public CommentResponse updateComment(Long commentId, Comment comment) {
        Comment existingComment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy comment với id: " + commentId));

        if (comment.getContent() != null) {
            existingComment.setContent(comment.getContent());
            existingComment.setIsEdited(true);
        }

        existingComment.setUpdatedAt(LocalDateTime.now());

        Comment saved = commentRepository.save(existingComment);
        return commentMapper.toCommentResponse(saved);
    }

    @Override
    @Transactional
    public void deleteComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy comment với id: " + commentId));

        comment.setIsDeleted(true);
        comment.setUpdatedAt(LocalDateTime.now());
        commentRepository.save(comment);
    }
}
