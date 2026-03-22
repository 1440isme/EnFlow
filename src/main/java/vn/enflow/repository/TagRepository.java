package vn.enflow.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.enflow.entity.Tag;

import java.util.List;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    List<Tag> findByWorkspace_WorkspaceId(Long workspaceId);
}
