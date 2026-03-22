package vn.enflow.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.enflow.entity.Workspace;

import java.util.List;

@Repository
public interface WorkspaceRepository extends JpaRepository<Workspace, Long> {
    boolean existsByWorkspaceKey(String workspaceKey);
    List<Workspace> findByOwner_UserId(Long ownerUserId);
}
