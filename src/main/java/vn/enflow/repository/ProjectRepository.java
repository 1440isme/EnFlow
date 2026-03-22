package vn.enflow.repository;

import vn.enflow.entity.Project;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository {
    Optional<Project> findByIdProject(Long idProject);
    List<Project> findByWorkspace();

}
