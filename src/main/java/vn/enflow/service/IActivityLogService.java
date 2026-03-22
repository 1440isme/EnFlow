package vn.enflow.service;

import vn.enflow.dto.request.ActivityLogCreationRequest;
import vn.enflow.dto.request.ActivityLogUpdateRequest;
import vn.enflow.dto.respone.ActivityLogResponse;

import java.util.List;

public interface IActivityLogService {
    ActivityLogResponse createActivityLog(Long taskId, ActivityLogCreationRequest request);
    
    ActivityLogResponse getActivityLogById(Long logId);
    
    List<ActivityLogResponse> getActivityLogsByWorkspaceId(Long workspaceId);
    
    List<ActivityLogResponse> getActivityLogsByProjectId(Long projectId);
    
    List<ActivityLogResponse> getActivityLogsByTaskId(Long taskId);
    
    ActivityLogResponse updateActivityLog(Long logId, ActivityLogUpdateRequest request);
    
    void deleteActivityLog(Long logId);
}
