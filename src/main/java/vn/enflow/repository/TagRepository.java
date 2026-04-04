package vn.enflow.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.enflow.entity.Tag;

import java.util.List;
import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    List<Tag> findByWorkspace_WorkspaceId(Long workspaceId);

    @Query("SELECT t.workspace.workspaceId FROM Tag t WHERE t.tagId = :tagId")
    Optional<Long> findWorkspaceIdByTagId(@Param("tagId") Long tagId);
}
