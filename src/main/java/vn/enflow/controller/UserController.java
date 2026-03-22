package vn.enflow.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.enflow.dto.request.ChangePasswordRequest;
import vn.enflow.dto.request.UserCreationRequest;
import vn.enflow.dto.request.UserUpdateRequest;
import vn.enflow.dto.respone.UserResponse;
import vn.enflow.service.IUserService;

import java.util.List;

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

    @GetMapping
    ResponseEntity<List<UserResponse>> findAll() {
        return ResponseEntity.ok(userService.findAll());
    }

    @GetMapping("/{userId}")
    ResponseEntity<UserResponse> findById(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.findById(userId));
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
