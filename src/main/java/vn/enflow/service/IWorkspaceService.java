package vn.enflow.service;

import vn.enflow.dto.request.WorkspaceRequest;
import vn.enflow.dto.request.WorkspaceUpdateRequest;
import vn.enflow.dto.respone.WorkspaceResponse;

import java.util.List;

public interface IWorkspaceService {
    WorkspaceResponse create(WorkspaceRequest request);
    WorkspaceResponse findById(Long id);
    List<WorkspaceResponse> findAll();
    List<WorkspaceResponse> findByOwner(Long ownerUserId);
    WorkspaceResponse update(Long id, WorkspaceUpdateRequest request);
    void delete(Long id);
}
