package vn.enflow.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskAssigneeId implements Serializable {

    @Column(name = "task_id")
    Long taskId;

    @Column(name = "user_id")
    Long userId;
}
