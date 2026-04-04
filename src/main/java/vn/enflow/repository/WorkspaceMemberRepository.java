package vn.enflow.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import vn.enflow.entity.WorkspaceMember;
import vn.enflow.entity.WorkspaceMemberId;

import java.util.List;
import java.util.Optional;

@Repository
public interface WorkspaceMemberRepository extends JpaRepository<WorkspaceMember, WorkspaceMemberId> {
    List<WorkspaceMember> findById_WorkspaceId(Long workspaceId);
    List<WorkspaceMember> findById_UserId(Long userId);

    Optional<WorkspaceMember> findById_WorkspaceIdAndId_UserId(Long workspaceId, Long userId);

    /** Chuẩn hóa dữ liệu cũ (admin) trước khi enum Java bỏ giá trị đó. */
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Transactional
    @Query(value = "UPDATE workspace_members SET role_in_workspace = 'member' WHERE role_in_workspace = 'admin'", nativeQuery = true)
    int migrateLegacyAdminRoles();
}
