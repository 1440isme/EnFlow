package vn.enflow.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.enflow.entity.Notification;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findTop50ByUser_UserIdOrderByCreatedAtDesc(Long userId);

    List<Notification> findTop50ByUser_UserIdAndWorkspaceIdOrderByCreatedAtDesc(Long userId, Long workspaceId);

    long countByUser_UserIdAndReadAtIsNull(Long userId);

    long countByUser_UserIdAndWorkspaceIdAndReadAtIsNull(Long userId, Long workspaceId);

    Optional<Notification> findByNotificationIdAndUser_UserId(Long notificationId, Long userId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("UPDATE Notification n SET n.readAt = :readAt WHERE n.user.userId = :userId AND n.readAt IS NULL "
            + "AND (:workspaceId IS NULL OR n.workspaceId = :workspaceId)")
    int markAllReadForUser(
            @Param("userId") Long userId,
            @Param("workspaceId") Long workspaceId,
            @Param("readAt") LocalDateTime readAt);
}
