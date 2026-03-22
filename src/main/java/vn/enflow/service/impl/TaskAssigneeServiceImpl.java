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
import vn.enflow.service.ITaskAssigneeService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TaskAssigneeServiceImpl implements ITaskAssigneeService {

    TaskAssigneeRepository taskAssigneeRepository;
    TaskRepository taskRepository;
    UserRepository userRepository;
    TaskAssigneeMapper taskAssigneeMapper;

    @Override
    @Transactional
    public TaskAssigneeResponse addAssignee(Long taskId, TaskAssigneeRequest request) {
        if (request.getUserId() == null) {
            throw new RuntimeException("userId là bắt buộc");
        }

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy task với id: " + taskId));

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

        return taskAssigneeMapper.toTaskAssigneeResponse(taskAssigneeRepository.save(assignee));
    }

    @Override
    public List<TaskAssigneeResponse> getAssigneesByTaskId(Long taskId) {
        if (!taskRepository.existsById(taskId)) {
            throw new RuntimeException("Không tìm thấy task với id: " + taskId);
        }

        return taskAssigneeRepository.findById_TaskId(taskId).stream()
                .map(taskAssigneeMapper::toTaskAssigneeResponse)
                .toList();
    }

    @Override
    public List<TaskAssigneeResponse> getAssignmentsByUserId(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("Không tìm thấy user với id: " + userId);
        }

        return taskAssigneeRepository.findById_UserId(userId).stream()
                .map(taskAssigneeMapper::toTaskAssigneeResponse)
                .toList();
    }

    @Override
    @Transactional
    public TaskAssigneeResponse updateAssignee(Long taskId, Long userId, TaskAssigneeUpdateRequest request) {
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
