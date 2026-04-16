package vn.enflow.service.impl;

import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import vn.enflow.dto.request.TaskAssigneeRequest;
import vn.enflow.dto.request.TaskAssigneeUpdateRequest;
import vn.enflow.dto.respone.TaskAssigneeResponse;
import vn.enflow.entity.Task;
import vn.enflow.entity.TaskAssignee;
import vn.enflow.entity.TaskAssigneeId;
import vn.enflow.entity.User;
import vn.enflow.mapper.TaskAssigneeMapper;
import vn.enflow.repository.TaskAssigneeRepository;
import vn.enflow.repository.TaskRepository;
import vn.enflow.repository.UserRepository;
import vn.enflow.security.SecurityUtils;
import vn.enflow.service.INotificationService;
import vn.enflow.service.ITaskAssigneeService;
import vn.enflow.service.WorkspaceAccessService;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TaskAssigneeServiceImpl implements ITaskAssigneeService {

    TaskAssigneeRepository taskAssigneeRepository;
    TaskRepository taskRepository;
    UserRepository userRepository;
    TaskAssigneeMapper taskAssigneeMapper;
    INotificationService notificationService;
    WorkspaceAccessService workspaceAccessService;

    @Override
    @Transactional
    public TaskAssigneeResponse addAssignee(Long taskId, TaskAssigneeRequest request) {
        if (request.getUserId() == null) {
            throw new RuntimeException("userId là bắt buộc");
        }

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy task với id: " + taskId));
        workspaceAccessService.requireOwnerAccess(task.getProject().getWorkspace().getWorkspaceId(), SecurityUtils.currentUserId());

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy user với id: " + request.getUserId()));

        TaskAssigneeId assigneeId = new TaskAssigneeId(taskId, request.getUserId());
        if (taskAssigneeRepository.existsById(assigneeId)) {
            throw new RuntimeException("User đã được assign vào task này");
        }

        boolean isPrimary = Boolean.TRUE.equals(request.getIsPrimary());
        if (isPrimary) {
            clearPrimaryAssignee(taskId);
        }

        TaskAssignee assignee = TaskAssignee.builder()
                .id(assigneeId)
                .task(task)
                .user(user)
                .assignedAt(LocalDateTime.now())
                .isPrimary(isPrimary)
                .build();

        TaskAssigneeResponse response =
                taskAssigneeMapper.toTaskAssigneeResponse(taskAssigneeRepository.save(assignee));

        try {
            Long actorId = SecurityUtils.currentUserId();
            if (!request.getUserId().equals(actorId)) {
                notificationService.createTaskAssignedNotification(user, task);
            }
        } catch (Exception ignored) {
            /* thông báo không làm fail gán task */
        }

        return response;
    }

    @Override
    public List<TaskAssigneeResponse> getAssigneesByTaskId(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy task với id: " + taskId));
        workspaceAccessService.requireActiveMembership(task.getProject().getWorkspace().getWorkspaceId(), SecurityUtils.currentUserId());

        return taskAssigneeRepository.findById_TaskId(taskId).stream()
                .map(taskAssigneeMapper::toTaskAssigneeResponse)
                .toList();
    }

    @Override
    public Map<Long, List<TaskAssigneeResponse>> getAssigneesByTaskIds(List<Long> taskIds) {
        if (taskIds == null || taskIds.isEmpty()) {
            return Map.of();
        }
        List<Long> ids = taskIds.stream().filter(id -> id != null && id > 0).distinct().toList();
        if (ids.isEmpty()) {
            return Map.of();
        }

        // Check access per task (workspace membership) to avoid leaking assignees across workspaces/projects.
        Long currentUserId = SecurityUtils.currentUserId();
        List<Task> tasks = taskRepository.findAllById(ids);
        Map<Long, Task> taskById = tasks.stream().collect(Collectors.toMap(Task::getTaskId, t -> t, (a, b) -> a));
        for (Long id : ids) {
            Task t = taskById.get(id);
            if (t == null) {
                continue;
            }
            workspaceAccessService.requireActiveMembership(t.getProject().getWorkspace().getWorkspaceId(), currentUserId);
        }

        Map<Long, List<TaskAssigneeResponse>> out = new HashMap<>();
        for (Long id : ids) {
            out.put(id, List.of());
        }
        taskAssigneeRepository.findById_TaskIdIn(ids).forEach(row -> {
            Long tid = row.getId().getTaskId();
            out.computeIfAbsent(tid, k -> new java.util.ArrayList<>())
                    .add(taskAssigneeMapper.toTaskAssigneeResponse(row));
        });
        return out;
    }

    @Override
    public List<TaskAssigneeResponse> getAssignmentsByUserId(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("Không tìm thấy user với id: " + userId);
        }
        if (!SecurityUtils.currentUserId().equals(userId)) {
            throw new RuntimeException("Không có quyền xem danh sách task của người dùng khác");
        }

        return taskAssigneeRepository.findById_UserId(userId).stream()
                .map(taskAssigneeMapper::toTaskAssigneeResponse)
                .toList();
    }

    @Override
    public List<TaskAssigneeResponse> getAssignmentsByUserIdAndWorkspaceId(Long userId, Long workspaceId) {
        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("Không tìm thấy user với id: " + userId);
        }
        workspaceAccessService.requireCurrentUserActiveMember(workspaceId);
        if (!SecurityUtils.currentUserId().equals(userId)) {
            throw new RuntimeException("Không có quyền xem danh sách task của người dùng khác");
        }

        return taskAssigneeRepository.findByUserIdAndWorkspaceId(userId, workspaceId).stream()
                .map(taskAssigneeMapper::toTaskAssigneeResponse)
                .toList();
    }

    @Override
    @Transactional
    public TaskAssigneeResponse updateAssignee(Long taskId, Long userId, TaskAssigneeUpdateRequest request) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy task với id: " + taskId));
        workspaceAccessService.requireOwnerAccess(task.getProject().getWorkspace().getWorkspaceId(), SecurityUtils.currentUserId());
        TaskAssigneeId assigneeId = new TaskAssigneeId(taskId, userId);
        TaskAssignee assignee = taskAssigneeRepository.findById(assigneeId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy assignee của task"));

        if (request.getIsPrimary() != null) {
            if (Boolean.TRUE.equals(request.getIsPrimary())) {
                clearPrimaryAssignee(taskId);
            }
            assignee.setIsPrimary(request.getIsPrimary());
        }

        return taskAssigneeMapper.toTaskAssigneeResponse(taskAssigneeRepository.save(assignee));
    }

    @Override
    @Transactional
    public void removeAssignee(Long taskId, Long userId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy task với id: " + taskId));
        workspaceAccessService.requireOwnerAccess(task.getProject().getWorkspace().getWorkspaceId(), SecurityUtils.currentUserId());
        TaskAssigneeId assigneeId = new TaskAssigneeId(taskId, userId);
        if (!taskAssigneeRepository.existsById(assigneeId)) {
            throw new RuntimeException("Không tìm thấy assignee của task");
        }

        taskAssigneeRepository.deleteById(assigneeId);
    }

    private void clearPrimaryAssignee(Long taskId) {
        List<TaskAssignee> assignees = taskAssigneeRepository.findById_TaskId(taskId);
        for (TaskAssignee assignee : assignees) {
            if (Boolean.TRUE.equals(assignee.getIsPrimary())) {
                assignee.setIsPrimary(false);
            }
        }
        taskAssigneeRepository.saveAll(assignees);
    }
}
