package vn.enflow.service;

import java.util.List;
import java.util.Optional;

import vn.enflow.dto.request.ChangePasswordRequest;
import vn.enflow.dto.request.UpdateMyProfileRequest;
import vn.enflow.dto.request.UserCreationRequest;
import vn.enflow.dto.request.UserUpdateRequest;
import vn.enflow.dto.respone.UserPublicLookupResponse;
import vn.enflow.dto.respone.UserResponse;
import vn.enflow.entity.User;

public interface IUserService {

    UserResponse createUser(UserCreationRequest request);

    /**
     * Tra cứu user theo email (đã chuẩn hóa) để hiển thị tên khi mời vào workspace.
     * Không tìm thấy hoặc tài khoản vô hiệu → lỗi 404.
     */
    UserPublicLookupResponse lookupByEmail(String emailNormalized);

    UserResponse updateMyProfile(Long userId, UpdateMyProfileRequest request);

    UserResponse updateUser(Long userId, UserUpdateRequest request);

    UserResponse findById(Long id);

    List<UserResponse> findAll();

    void deleteById(Long id);

    void changePassword(Long userId, ChangePasswordRequest request);

    void deactivateAccount(Long userId);

    // Các method phụ trợ nội bộ
    void delete(User user);

    long count();

    Optional<User> findByIdOptional(Long id);

    List<User> findAllById(Iterable<Long> ids);

    <S extends User> S save(S entity);
}
