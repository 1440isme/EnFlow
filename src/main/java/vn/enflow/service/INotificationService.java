package vn.enflow.service;

import vn.enflow.dto.respone.NotificationFeedResponse;
import vn.enflow.dto.respone.NotificationResponse;
import vn.enflow.entity.Task;
import vn.enflow.entity.User;

public interface INotificationService {

    NotificationFeedResponse listMine(Long workspaceId);

    NotificationResponse markRead(Long notificationId);

    void markAllRead(Long workspaceId);

    /** Gọi khi user được thêm làm assignee (không throw — lỗi ghi DB không làm hỏng luồng chính). */
    void createTaskAssignedNotification(User assignee, Task task);
}
