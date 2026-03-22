package vn.enflow.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.enflow.entity.WorkspaceMember;
import vn.enflow.entity.WorkspaceMemberId;

import java.util.List;

@Repository
public interface WorkspaceMemberRepository extends JpaRepository<WorkspaceMember, WorkspaceMemberId> {
    List<WorkspaceMember> findById_WorkspaceId(Long workspaceId);
    List<WorkspaceMember> findById_UserId(Long userId);
}
