package vn.enflow.service.impl;

import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import vn.enflow.dto.respone.NotificationFeedResponse;
import vn.enflow.dto.respone.NotificationResponse;
import vn.enflow.entity.Notification;
import vn.enflow.entity.Task;
import vn.enflow.entity.User;
import vn.enflow.repository.NotificationRepository;
import vn.enflow.security.SecurityUtils;
import vn.enflow.service.INotificationService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NotificationServiceImpl implements INotificationService {

    NotificationRepository notificationRepository;

    @Override
    public NotificationFeedResponse listMine(Long workspaceId) {
        Long userId = SecurityUtils.currentUserId();
        List<Notification> rows =
                workspaceId != null
                        ? notificationRepository.findTop50ByUser_UserIdAndWorkspaceIdOrderByCreatedAtDesc(
                                userId, workspaceId)
                        : notificationRepository.findTop50ByUser_UserIdOrderByCreatedAtDesc(userId);

        long unread =
                workspaceId != null
                        ? notificationRepository.countByUser_UserIdAndWorkspaceIdAndReadAtIsNull(
                                userId, workspaceId)
                        : notificationRepository.countByUser_UserIdAndReadAtIsNull(userId);

        return NotificationFeedResponse.builder()
                .unreadCount(unread)
                .items(rows.stream().map(this::toResponse).toList())
                .build();
    }

    @Override
    @Transactional
    public NotificationResponse markRead(Long notificationId) {
        Long userId = SecurityUtils.currentUserId();
        Notification n =
                notificationRepository
                        .findByNotificationIdAndUser_UserId(notificationId, userId)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không có thông báo"));
        if (n.getReadAt() == null) {
            n.setReadAt(LocalDateTime.now());
            n = notificationRepository.save(n);
        }
        return toResponse(n);
    }

    @Override
    @Transactional
    public void markAllRead(Long workspaceId) {
        Long userId = SecurityUtils.currentUserId();
        notificationRepository.markAllReadForUser(userId, workspaceId, LocalDateTime.now());
    }

    @Override
    @Transactional
    public void createTaskAssignedNotification(User assignee, Task task) {
        try {
            Long wsId = task.getProject().getWorkspace().getWorkspaceId();
            Long projectId = task.getProject().getProjectId();
            Notification n =
                    Notification.builder()
                            .user(assignee)
                            .workspaceId(wsId)
                            .type("TASK_ASSIGNED")
                            .title("Bạn được giao task")
                            .body(task.getTitle())
                            .taskId(task.getTaskId())
                            .projectId(projectId)
                            .readAt(null)
                            .createdAt(LocalDateTime.now())
                            .build();
            notificationRepository.save(n);
        } catch (Exception ignored) {
            // Không làm fail luồng gán task
        }
    }

    private NotificationResponse toResponse(Notification n) {
        return NotificationResponse.builder()
                .notificationId(n.getNotificationId())
                .workspaceId(n.getWorkspaceId())
                .type(n.getType())
                .title(n.getTitle())
                .body(n.getBody())
                .taskId(n.getTaskId())
                .projectId(n.getProjectId())
                .readAt(n.getReadAt())
                .createdAt(n.getCreatedAt())
                .build();
    }
}
