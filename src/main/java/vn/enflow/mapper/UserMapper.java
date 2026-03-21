package vn.enflow.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import vn.enflow.dto.request.UserCreationRequest;
import vn.enflow.dto.request.UserUpdateRequest;
import vn.enflow.dto.respone.UserResponse;
import vn.enflow.entity.User;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {

    // UserCreationRequest → User
    // Bỏ qua: userId (auto-gen), updatedAt (chưa có khi tạo), các collection
    // relationship
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "ownedWorkspaces", ignore = true)
    @Mapping(target = "workspaceMemberships", ignore = true)
    User toUser(UserCreationRequest request);

    // User → UserResponse

    UserResponse toUserResponse(User user);

    // UserUpdateRequest → User (cập nhật một phần, IGNORE nếu null)
    // Bỏ qua: các field không được phép thay đổi qua update thông thường
    // password đổi riêng qua ChangePasswordRequest → passwordHash ignore ở đây
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "username", ignore = true)
    @Mapping(target = "passwordHash", ignore = true)
    @Mapping(target = "fullName", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "ownedWorkspaces", ignore = true)
    @Mapping(target = "workspaceMemberships", ignore = true)
    void updateUser(@MappingTarget User user, UserUpdateRequest request);
}
