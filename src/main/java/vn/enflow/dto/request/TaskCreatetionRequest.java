package vn.enflow.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TaskCreatetionRequest {
    Long parentTaskId;
    String taskCode;
    String title;
    String description;
    String taskType;
    String priority;
    Long reporterId;
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
}
