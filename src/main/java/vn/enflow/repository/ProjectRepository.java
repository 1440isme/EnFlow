package vn.enflow.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.enflow.entity.Project;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findByWorkspace_WorkspaceId(Long workspaceId);
    boolean existsByProjectKey(String projectKey);

    @Query("SELECT p.workspace.workspaceId FROM Project p WHERE p.projectId = :projectId")
    Optional<Long> findWorkspaceIdByProjectId(@Param("projectId") Long projectId);
}
