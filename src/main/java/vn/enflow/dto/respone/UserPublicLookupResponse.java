package vn.enflow.dto.respone;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Thông tin tối thiểu khi tra cứu user theo email (ví dụ màn hình mời vào workspace).
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserPublicLookupResponse {
    private Long userId;
    private String fullName;
    private String email;
    private String avatarUrl;
}
