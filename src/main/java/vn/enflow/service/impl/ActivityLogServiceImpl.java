package vn.enflow.service.impl;

import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import vn.enflow.dto.request.ActivityLogCreationRequest;
import vn.enflow.dto.request.ActivityLogUpdateRequest;
import vn.enflow.dto.respone.ActivityLogResponse;
import vn.enflow.entity.ActivityLog;
import vn.enflow.entity.Project;
import vn.enflow.entity.Task;
import vn.enflow.entity.User;
import vn.enflow.entity.Workspace;
import vn.enflow.mapper.ActivityLogMapper;
import vn.enflow.repository.ActivityLogRepository;
import vn.enflow.repository.ProjectRepository;
import vn.enflow.repository.TaskRepository;
import vn.enflow.repository.UserRepository;
import vn.enflow.repository.WorkspaceRepository;
import vn.enflow.service.IActivityLogService;
import vn.enflow.service.WorkspaceAccessService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ActivityLogServiceImpl implements IActivityLogService {

    ActivityLogRepository activityLogRepository;
    WorkspaceRepository workspaceRepository;
    WorkspaceAccessService workspaceAccessService;
    ProjectRepository projectRepository;
    TaskRepository taskRepository;
    UserRepository userRepository;
    ActivityLogMapper activityLogMapper;

    @Override
    @Transactional
    public ActivityLogResponse createActivityLog(Long taskId, ActivityLogCreationRequest request) {
        if (request.getWorkspaceId() == null) {
            throw new RuntimeException("workspaceId là bắt buộc");
        }
        if (request.getProjectId() == null) {
            throw new RuntimeException("projectId là bắt buộc");
        }
        if (request.getActorId() == null) {
            throw new RuntimeException("actorId là bắt buộc");
        }
        if (request.getAction() == null || request.getAction().isBlank()) {
            throw new RuntimeException("action là bắt buộc");
        }

        Workspace workspace = workspaceRepository.findById(request.getWorkspaceId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy workspace với id: " + request.getWorkspaceId()));

        Project project = projectRepository.findById(request.getProjectId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy project với id: " + request.getProjectId()));

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy task với id: " + taskId));

        User actor = userRepository.findById(request.getActorId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy user với id: " + request.getActorId()));

        if (!task.getProject().getProjectId().equals(project.getProjectId())) {
            throw new RuntimeException("Task " + taskId + " không thuộc project " + request.getProjectId());
        }

        if (!project.getWorkspace().getWorkspaceId().equals(workspace.getWorkspaceId())) {
            throw new RuntimeException("Project " + request.getProjectId() + " không thuộc workspace " + request.getWorkspaceId());
        }

        ActivityLog activityLog = activityLogMapper.toActivityLog(request);
        activityLog.setTaskId(task.getTaskId());
        activityLog.setProjectId(project.getProjectId());
        activityLog.setWorkspaceId(workspace.getWorkspaceId());
        activityLog.setActorId(actor.getUserId());
        activityLog.setCreatedAt(LocalDateTime.now());

        ActivityLog saved = activityLogRepository.save(activityLog);
        return activityLogMapper.toActivityLogResponse(saved);
    }

    @Override
    public ActivityLogResponse getActivityLogById(Long logId) {
        ActivityLog activityLog = activityLogRepository.findById(logId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy activity log với id: " + logId));
        return activityLogMapper.toActivityLogResponse(activityLog);
    }

    @Override
    public List<ActivityLogResponse> getActivityLogsByWorkspaceId(Long workspaceId) {
        if (!workspaceRepository.existsById(workspaceId)) {
            throw new RuntimeException("Không tìm thấy workspace với id: " + workspaceId);
        }
        workspaceAccessService.requireCurrentUserActiveMember(workspaceId);
        return activityLogRepository.findByWorkspaceId(workspaceId).stream()
                .map(activityLogMapper::toActivityLogResponse)
                .toList();
    }

    @Override
    public List<ActivityLogResponse> getActivityLogsByProjectId(Long projectId) {
        if (!projectRepository.existsById(projectId)) {
            throw new RuntimeException("Không tìm thấy project với id: " + projectId);
        }
        return activityLogRepository.findByProjectId(projectId).stream()
                .map(activityLogMapper::toActivityLogResponse)
                .toList();
    }

    @Override
    public List<ActivityLogResponse> getActivityLogsByTaskId(Long taskId) {
        if (!taskRepository.existsById(taskId)) {
            throw new RuntimeException("Không tìm thấy task với id: " + taskId);
        }
        return activityLogRepository.findByTaskId(taskId).stream()
                .map(activityLogMapper::toActivityLogResponse)
                .toList();
    }

    @Override
    @Transactional
    public ActivityLogResponse updateActivityLog(Long logId, ActivityLogUpdateRequest request) {
        ActivityLog activityLog = activityLogRepository.findById(logId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy activity log với id: " + logId));

        activityLogMapper.updateActivityLog(activityLog, request);

        ActivityLog saved = activityLogRepository.save(activityLog);
        return activityLogMapper.toActivityLogResponse(saved);
    }

    @Override
    @Transactional
    public void deleteActivityLog(Long logId) {
        if (!activityLogRepository.existsById(logId)) {
            throw new RuntimeException("Không tìm thấy activity log với id: " + logId);
        }
        activityLogRepository.deleteById(logId);
    }
}
