package vn.enflow.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.enflow.entity.ProjectList;

import java.util.List;

@Repository
public interface ProjectListRepository extends JpaRepository<ProjectList, Long> {
    List<ProjectList> findByProject_ProjectId(Long projectId);
}
