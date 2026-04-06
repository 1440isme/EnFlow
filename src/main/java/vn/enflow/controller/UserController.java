package vn.enflow.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import vn.enflow.dto.request.ChangePasswordRequest;
import vn.enflow.dto.request.UpdateMyProfileRequest;
import vn.enflow.dto.request.UserCreationRequest;
import vn.enflow.dto.request.UserUpdateRequest;
import vn.enflow.dto.respone.UserPublicLookupResponse;
import vn.enflow.dto.respone.UserResponse;
import vn.enflow.security.SecurityUtils;
import vn.enflow.service.IUserService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {

    IUserService userService;

    @PostMapping
    ResponseEntity<UserResponse> createUser(@RequestBody UserCreationRequest request) {
        return ResponseEntity.ok(userService.createUser(request));
    }

    /** Tài khoản hiện tại (JWT) — đặt trước /{userId} để không nhầm "me" với id. */
    @GetMapping("/me")
    ResponseEntity<UserResponse> getCurrentProfile() {
        return ResponseEntity.ok(userService.findById(SecurityUtils.currentUserId()));
    }

    @PutMapping("/me")
    ResponseEntity<UserResponse> updateCurrentProfile(@RequestBody UpdateMyProfileRequest request) {
        return ResponseEntity.ok(userService.updateMyProfile(SecurityUtils.currentUserId(), request));
    }

    @PatchMapping("/me/change-password")
    ResponseEntity<Void> changeMyPassword(@RequestBody ChangePasswordRequest request) {
        userService.changePassword(SecurityUtils.currentUserId(), request);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/me/deactivate")
    ResponseEntity<Void> deactivateMyAccount() {
        userService.deactivateAccount(SecurityUtils.currentUserId());
        return ResponseEntity.noContent().build();
    }

    /**
     * Tra cứu user theo email để hiển thị tên (ví dụ trước khi thêm vào workspace).
     * Query: ?email=user@example.com (đã đăng nhập).
     */
    @GetMapping("/lookup")
    ResponseEntity<UserPublicLookupResponse> lookupByEmail(@RequestParam String email) {
        String normalized = normalizeEmailParam(email);
        UserPublicLookupResponse found = userService.lookupByEmail(normalized);
        if (found.getUserId().equals(SecurityUtils.currentUserId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Không thể mời chính mình");
        }
        return ResponseEntity.ok(found);
    }

    private static String normalizeEmailParam(String email) {
        if (email == null || email.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Thiếu email");
        }
        String e = email.trim().toLowerCase();
        int at = e.indexOf('@');
        if (at <= 0 || at == e.length() - 1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email không hợp lệ");
        }
        return e;
    }

    @GetMapping
    ResponseEntity<List<UserResponse>> findAll() {
        return ResponseEntity.ok(userService.findAll());
    }

    @GetMapping("/{userId}")
    ResponseEntity<UserResponse> findById(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.findById(userId));
    }

    @PostMapping("/batch")
    ResponseEntity<Map<Long, UserResponse>> findByIds(@RequestBody List<Long> userIds) {
        if (userIds == null || userIds.isEmpty()) {
            return ResponseEntity.ok(Map.of());
        }
        List<Long> ids = userIds.stream()
                .filter(id -> id != null && id > 0)
                .distinct()
                .toList();
        if (ids.isEmpty()) {
            return ResponseEntity.ok(Map.of());
        }
        return ResponseEntity.ok(
                userService.findByIds(ids).stream()
                        .collect(Collectors.toMap(UserResponse::getUserId, r -> r, (a, b) -> a))
        );
    }

    @PutMapping("/{userId}")
    ResponseEntity<UserResponse> updateUser(@PathVariable Long userId,
                                            @RequestBody UserUpdateRequest request) {
        return ResponseEntity.ok(userService.updateUser(userId, request));
    }

    @DeleteMapping("/{userId}")
    ResponseEntity<Void> deleteById(@PathVariable Long userId) {
        userService.deleteById(userId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{userId}/change-password")
    ResponseEntity<Void> changePassword(@PathVariable Long userId,
                                        @RequestBody ChangePasswordRequest request) {
        userService.changePassword(userId, request);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{userId}/deactivate")
    ResponseEntity<Void> deactivateAccount(@PathVariable Long userId) {
        userService.deactivateAccount(userId);
        return ResponseEntity.noContent().build();
    }
}
