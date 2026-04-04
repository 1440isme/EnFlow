package vn.enflow.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.enflow.dto.respone.NotificationFeedResponse;
import vn.enflow.dto.respone.NotificationResponse;
import vn.enflow.service.INotificationService;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NotificationController {

    INotificationService notificationService;

    @GetMapping
    ResponseEntity<NotificationFeedResponse> list(@RequestParam(required = false) Long workspaceId) {
        return ResponseEntity.ok(notificationService.listMine(workspaceId));
    }

    @PatchMapping("/{notificationId}/read")
    ResponseEntity<NotificationResponse> markRead(@PathVariable Long notificationId) {
        return ResponseEntity.ok(notificationService.markRead(notificationId));
    }

    @PostMapping("/read-all")
    ResponseEntity<Void> markAllRead(@RequestParam(required = false) Long workspaceId) {
        notificationService.markAllRead(workspaceId);
        return ResponseEntity.noContent().build();
    }
}
