package vn.enflow.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.enflow.entity.Workspace;

import java.util.List;

@Repository
public interface WorkspaceRepository extends JpaRepository<Workspace, Long> {
    boolean existsByWorkspaceKey(String workspaceKey);
    List<Workspace> findByOwner_UserId(Long ownerUserId);

    @Query("SELECT DISTINCT w FROM Workspace w JOIN WorkspaceMember m ON m.workspace.workspaceId = w.workspaceId "
            + "WHERE m.id.userId = :userId AND m.isActive = true ORDER BY w.name ASC")
    List<Workspace> findAllByActiveMemberUserId(@Param("userId") Long userId);
}
