package vn.enflow.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.enflow.entity.TaskAssignee;
import vn.enflow.entity.TaskAssigneeId;

import java.util.List;

@Repository
public interface TaskAssigneeRepository extends JpaRepository<TaskAssignee, TaskAssigneeId> {
    List<TaskAssignee> findById_TaskId(Long taskId);
    List<TaskAssignee> findById_UserId(Long userId);

    @Query(
            "SELECT ta FROM TaskAssignee ta JOIN FETCH ta.task t JOIN FETCH ta.user u "
                    + "JOIN t.project p JOIN p.workspace w "
                    + "WHERE ta.id.userId = :userId AND w.workspaceId = :workspaceId")
    List<TaskAssignee> findByUserIdAndWorkspaceId(@Param("userId") Long userId, @Param("workspaceId") Long workspaceId);
}
