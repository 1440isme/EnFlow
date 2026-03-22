package vn.enflow.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.enflow.entity.Status;

import java.util.List;

@Repository
public interface StatusRepository extends JpaRepository<Status, Long> {
    List<Status> findByProject_ProjectId(Long projectId);
    List<Status> findByList_ListId(Long listId);
}
