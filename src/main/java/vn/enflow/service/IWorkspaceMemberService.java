package vn.enflow.service;

import vn.enflow.dto.request.WorkspaceMemberRequest;
import vn.enflow.dto.request.WorkspaceMemberUpdateRequest;
import vn.enflow.dto.respone.WorkspaceMemberResponse;

import java.util.List;

public interface IWorkspaceMemberService {
    WorkspaceMemberResponse addMember(Long workspaceId, WorkspaceMemberRequest request);
    List<WorkspaceMemberResponse> getMembersByWorkspace(Long workspaceId);
    WorkspaceMemberResponse updateMember(Long workspaceId, Long userId, WorkspaceMemberUpdateRequest request);
    void removeMember(Long workspaceId, Long userId);
}
