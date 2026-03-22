package vn.enflow.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import vn.enflow.dto.request.CommentCreationRequest;
import vn.enflow.dto.request.CommentUpdateRequest;
import vn.enflow.dto.respone.CommentResponse;
import vn.enflow.entity.Comment;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CommentMapper {
    // CommentCreationRequest → Comment
    // Bỏ qua: commentId (auto-gen), createdAt (auto-gen), updatedAt (auto-gen), isEdited (mặc định false), isDeleted (mặc định false), các relationship
    @Mapping(target = "commentId", ignore = true)
    @Mapping(target = "taskId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "isEdited", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "task", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "parentComment", ignore = true)
    @Mapping(target = "replies", ignore = true)
    Comment toComment(CommentCreationRequest request);

    // Cập nhật một phần Comment, IGNORE nếu null
    @Mapping(target = "commentId", ignore = true)
    @Mapping(target = "taskId", ignore = true)
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "parentCommentId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "isEdited", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "task", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "parentComment", ignore = true)
    @Mapping(target = "replies", ignore = true)
    void updateComment(@MappingTarget Comment comment, CommentUpdateRequest request);

    CommentResponse toCommentResponse(Comment comment);
}
