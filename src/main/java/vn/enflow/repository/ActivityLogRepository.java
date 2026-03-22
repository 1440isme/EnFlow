package vn.enflow.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.enflow.entity.ActivityLog;

import java.util.List;

@Repository
public interface ActivityLogRepository extends JpaRepository<ActivityLog, Long> {
    List<ActivityLog> findByWorkspaceId(Long workspaceId);
    
    List<ActivityLog> findByProjectId(Long projectId);
    
    List<ActivityLog> findByTaskId(Long taskId);
}
