package vn.enflow.service.impl;

import jakarta.transaction.Transactional;
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
import vn.enflow.dto.request.WorkspaceRequest;
import vn.enflow.dto.respone.AuthResponse;
import vn.enflow.dto.respone.UserResponse;
import vn.enflow.entity.User;
import vn.enflow.mapper.UserMapper;
import vn.enflow.repository.UserRepository;
import vn.enflow.security.JwtService;
import vn.enflow.service.IAuthService;
import vn.enflow.service.IUserService;
import vn.enflow.service.IWorkspaceService;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthServiceImpl implements IAuthService {

    IUserService userService;
    IWorkspaceService workspaceService;
    UserRepository userRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;
    JwtService jwtService;

    @Override
    @Transactional(rollbackOn = Exception.class)
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
        String emailNormalized = normalizeEmailOrThrow(request.getEmail());
        // Username trong DB = email (định danh duy nhất; xác thực/xác minh sau này đều qua email).
        try {
            UserResponse created = userService.createUser(UserCreationRequest.builder()
                    .username(emailNormalized)
                    .email(emailNormalized)
                    .password(request.getPassword())
                    .fullName(request.getFullName().trim())
                    .build());
            workspaceService.create(WorkspaceRequest.builder()
                    .name(request.getFullName().trim() + " – Cá nhân")
                    .workspaceKey("personal-" + created.getUserId())
                    .description("Không gian cá nhân")
                    .ownerUserId(created.getUserId())
                    .isPrivate(true)
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
            throw new BadCredentialsException("Thiếu email hoặc mật khẩu");
        }
        String email = normalizeEmailForLogin(request.getUsernameOrEmail());
        if (email == null) {
            throw new BadCredentialsException("Email không hợp lệ");
        }
        User user = userRepository.findByEmail(email)
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

    /** Đăng ký: trim, lowercase, kiểm tra có @ hợp lệ. */
    private static String normalizeEmailOrThrow(String raw) {
        String email = raw.trim().toLowerCase();
        int at = email.indexOf('@');
        if (at <= 0 || at == email.length() - 1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email không hợp lệ");
        }
        return email;
    }

    /**
     * Đăng nhập: field vẫn là {@code usernameOrEmail} nhưng chỉ tra cứu theo email đã chuẩn hóa
     * (phải có dấu {@code @}).
     */
    private static String normalizeEmailForLogin(String raw) {
        String email = raw.trim().toLowerCase();
        int at = email.indexOf('@');
        if (at <= 0 || at == email.length() - 1) {
            return null;
        }
        return email;
    }
}
