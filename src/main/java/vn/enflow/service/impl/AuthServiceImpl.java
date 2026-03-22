package vn.enflow.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import vn.enflow.dto.request.LoginRequest;
import vn.enflow.dto.request.RegisterRequest;
import vn.enflow.dto.request.UserCreationRequest;
import vn.enflow.dto.respone.AuthResponse;
import vn.enflow.dto.respone.UserResponse;
import vn.enflow.entity.User;
import vn.enflow.mapper.UserMapper;
import vn.enflow.repository.UserRepository;
import vn.enflow.security.JwtService;
import vn.enflow.service.IAuthService;
import vn.enflow.service.IUserService;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthServiceImpl implements IAuthService {

    IUserService userService;
    UserRepository userRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;
    JwtService jwtService;

    @Override
    public AuthResponse register(RegisterRequest request) {
        if (!StringUtils.hasText(request.getFullName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Họ tên không được để trống");
        }
        if (!StringUtils.hasText(request.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email không được để trống");
        }
        if (!StringUtils.hasText(request.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Mật khẩu không được để trống");
        }
        String emailNormalized = request.getEmail().trim().toLowerCase();
        int at = emailNormalized.indexOf('@');
        if (at <= 0 || at == emailNormalized.length() - 1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email không hợp lệ");
        }
        String localPart = emailNormalized.substring(0, at);
        if (!StringUtils.hasText(localPart)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email không hợp lệ");
        }
        String username = resolveUniqueUsername(sanitizeUsernameLocalPart(localPart));
        try {
            UserResponse created = userService.createUser(UserCreationRequest.builder()
                    .username(username)
                    .email(emailNormalized)
                    .password(request.getPassword())
                    .fullName(request.getFullName().trim())
                    .build());
            User entity = userRepository.findById(created.getUserId())
                    .orElseThrow(() -> new IllegalStateException("Không tải lại được user vừa tạo"));
            return buildAuthResponse(entity, created);
        } catch (RuntimeException e) {
            if (e.getMessage() != null && e.getMessage().contains("tồn tại")) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
            }
            if (e.getMessage() != null && e.getMessage().contains("Mật khẩu")) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
            }
            throw e;
        }
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        if (!StringUtils.hasText(request.getUsernameOrEmail()) || !StringUtils.hasText(request.getPassword())) {
            throw new BadCredentialsException("Thiếu email/username hoặc mật khẩu");
        }
        String key = request.getUsernameOrEmail().trim();
        User user = resolveUserForLogin(key)
                .orElseThrow(() -> new BadCredentialsException("Sai thông tin đăng nhập"));
        if (Boolean.FALSE.equals(user.getIsActive())) {
            throw new BadCredentialsException("Tài khoản đã bị vô hiệu hóa");
        }
        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new BadCredentialsException("Sai thông tin đăng nhập");
        }
        return buildAuthResponse(user, userMapper.toUserResponse(user));
    }

    private AuthResponse buildAuthResponse(User entity, UserResponse userResponse) {
        return AuthResponse.builder()
                .accessToken(jwtService.generateToken(entity))
                .tokenType("Bearer")
                .expiresIn(jwtService.getExpirationMs() / 1000)
                .user(userResponse)
                .build();
    }

    /** Phần local của email, chỉ giữ ký tự an toàn cho username; rỗng thì fallback. */
    private static String sanitizeUsernameLocalPart(String local) {
        String cleaned = local.replaceAll("[^a-zA-Z0-9._-]", "_").replaceAll("_{2,}", "_");
        cleaned = cleaned.replaceAll("^_+|_+$", "");
        if (!StringUtils.hasText(cleaned)) {
            cleaned = "user";
        }
        if (cleaned.length() > 50) {
            cleaned = cleaned.substring(0, 50);
        }
        return cleaned.toLowerCase();
    }

    private String resolveUniqueUsername(String base) {
        String candidate = base;
        int n = 1;
        while (userRepository.existsByUsername(candidate)) {
            String suffix = "_" + n++;
            int maxBase = Math.max(1, 50 - suffix.length());
            candidate = base.substring(0, Math.min(base.length(), maxBase)) + suffix;
        }
        return candidate;
    }

    /** Có dấu @ → coi là email (chuẩn hóa lowercase); không có → username (không phân biệt hoa thường). */
    private Optional<User> resolveUserForLogin(String key) {
        if (key.contains("@")) {
            return userRepository.findByEmail(key.toLowerCase());
        }
        return userRepository.findByUsername(key.toLowerCase());
    }
}
