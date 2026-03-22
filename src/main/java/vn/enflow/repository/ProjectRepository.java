package vn.enflow.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.enflow.entity.Project;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findByWorkspace_WorkspaceId(Long workspaceId);
    boolean existsByProjectKey(String projectKey);
}
