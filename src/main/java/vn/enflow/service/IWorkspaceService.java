package vn.enflow.service;

import vn.enflow.dto.request.WorkspaceRequest;
import vn.enflow.dto.request.WorkspaceUpdateRequest;
import vn.enflow.dto.respone.WorkspaceResponse;

import java.util.List;

public interface IWorkspaceService {
    /** API đã đăng nhập: chỉ tạo workspace với owner = user hiện tại. */
    WorkspaceResponse create(WorkspaceRequest request);

    /**
     * Luồng đăng ký: chưa có JWT, không gọi {@code SecurityUtils}.
     * Chỉ dùng từ {@code AuthService} sau khi tạo user.
     */
    WorkspaceResponse createDuringRegistration(WorkspaceRequest request);

    WorkspaceResponse findById(Long id);

    /** Workspace mà user hiện tại là thành viên đang hoạt động. */
    List<WorkspaceResponse> findAccessibleForCurrentUser();

    List<WorkspaceResponse> findByOwner(Long ownerUserId);

    WorkspaceResponse update(Long id, WorkspaceUpdateRequest request);

    void delete(Long id);
}
