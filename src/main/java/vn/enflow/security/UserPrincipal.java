package vn.enflow.security;

import java.io.Serializable;

/**
 * Principal đặt vào {@link org.springframework.security.core.context.SecurityContext} sau khi xác thực JWT.
 */
public record UserPrincipal(Long userId, String username, String email) implements Serializable {}
