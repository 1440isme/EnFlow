package vn.enflow.dto.respone;

import java.time.LocalDateTime;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentResponse {
    Long commentId;
    
    Long taskId;

    Long userId;

    Long parentCommentId;

    String content;

    Boolean isEdited;

    Boolean isDeleted;

    LocalDateTime createdAt;

    LocalDateTime updatedAt;
}
