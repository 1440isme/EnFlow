package vn.enflow.service;

import vn.enflow.dto.request.TaskAssigneeRequest;
import vn.enflow.dto.request.TaskAssigneeUpdateRequest;
import vn.enflow.dto.respone.TaskAssigneeResponse;

import java.util.List;
import java.util.Map;

public interface ITaskAssigneeService {
    TaskAssigneeResponse addAssignee(Long taskId, TaskAssigneeRequest request);
    List<TaskAssigneeResponse> getAssigneesByTaskId(Long taskId);
    Map<Long, List<TaskAssigneeResponse>> getAssigneesByTaskIds(List<Long> taskIds);
    List<TaskAssigneeResponse> getAssignmentsByUserId(Long userId);

    /** Assignments whose task belongs to a project in the given workspace. */
    List<TaskAssigneeResponse> getAssignmentsByUserIdAndWorkspaceId(Long userId, Long workspaceId);
    TaskAssigneeResponse updateAssignee(Long taskId, Long userId, TaskAssigneeUpdateRequest request);
    void removeAssignee(Long taskId, Long userId);
}
