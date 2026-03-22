package vn.enflow.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.enflow.entity.TaskTag;
import vn.enflow.entity.TaskTagId;

import java.util.List;

@Repository
public interface TaskTagRepository extends JpaRepository<TaskTag, TaskTagId> {
    List<TaskTag> findById_TaskId(Long taskId);
    List<TaskTag> findById_TagId(Long tagId);
}
