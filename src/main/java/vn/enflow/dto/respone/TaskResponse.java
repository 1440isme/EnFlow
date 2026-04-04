package vn.enflow.dto.respone;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TaskResponse {
    Long taskId;
    Long projectId;
    String projectName;
    Long listId;
    String listName;
    Long statusId;
    Long parentTaskId;
    Long reporterId;
    String taskCode;
    String title;
    String description;
    String taskType;
    String priority;
    LocalDateTime startDate;
    LocalDateTime dueDate;
    LocalDateTime completedAt;
    String resolution;
    Double timeEstimateDays;
    Double timeSpentDays;
    Integer points;
    Integer position;
    Boolean isPrivate;
    Boolean archived;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
