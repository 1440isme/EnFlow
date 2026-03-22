package vn.enflow.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.enflow.entity.TaskAssignee;
import vn.enflow.entity.TaskAssigneeId;

import java.util.List;

@Repository
public interface TaskAssigneeRepository extends JpaRepository<TaskAssignee, TaskAssigneeId> {
    List<TaskAssignee> findById_TaskId(Long taskId);
    List<TaskAssignee> findById_UserId(Long userId);
}
