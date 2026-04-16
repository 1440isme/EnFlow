package vn.enflow.service.impl;

import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import vn.enflow.dto.request.TaskCreatetionRequest;
import vn.enflow.dto.request.TaskUpdateRequest;
import vn.enflow.dto.respone.TaskResponse;
import vn.enflow.entity.*;
import vn.enflow.mapper.TaskMapper;
import vn.enflow.repository.*;
import vn.enflow.security.SecurityUtils;
import vn.enflow.service.ITaskService;
import vn.enflow.service.WorkspaceAccessService;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.http.HttpStatus;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TaskServiceImpl implements ITaskService {

    TaskRepository taskRepository;
    ProjectRepository projectRepository;
    ProjectListRepository projectListRepository;
    StatusRepository statusRepository;
    UserRepository userRepository;
    TaskMapper taskMapper;
    WorkspaceAccessService workspaceAccessService;

    @Override
    @Transactional
    public TaskResponse createtionTask(Long projectId, Long listId, Long statusId, TaskCreatetionRequest request) {
        if (request.getReporterId() == null) {
            throw new RuntimeException("reporterId là bắt buộc");
        }

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy project với id: " + projectId));
        workspaceAccessService.requireCurrentUserOwnerAccess(project.getWorkspace().getWorkspaceId());

        ProjectList list = projectListRepository.findById(listId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy list với id: " + listId));

        Status status = statusRepository.findById(statusId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy status với id: " + statusId));

        if (!list.getProject().getProjectId().equals(projectId)) {
            throw new RuntimeException("List " + listId + " không thuộc project " + projectId);
        }

        if (!status.getProject().getProjectId().equals(projectId)) {
            throw new RuntimeException("Status " + statusId + " không thuộc project " + projectId);
        }

        if (!status.getList().getListId().equals(listId)) {
            throw new RuntimeException("Status " + statusId + " không thuộc list " + listId);
        }

        if (request.getTaskCode() != null && taskRepository.existsByTaskCode(request.getTaskCode())) {
            throw new RuntimeException("Task code đã tồn tại: " + request.getTaskCode());
        }

        User reporter = userRepository.findById(request.getReporterId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy reporter với id: " + request.getReporterId()));

        Task parentTask = getValidParentTask(request.getParentTaskId(), projectId, null);

        Task task = taskMapper.toTask(request);
        task.setProject(project);
        task.setList(list);
        task.setStatus(status);
        task.setReporter(reporter);
        task.setParentTask(parentTask);

        LocalDateTime now = LocalDateTime.now();
        task.setCreatedAt(now);
        task.setUpdatedAt(now);

        if (task.getPosition() == null) task.setPosition(0);
        if (task.getIsPrivate() == null) task.setIsPrivate(false);
        if (task.getArchived() == null) task.setArchived(false);

        Task saved = taskRepository.save(task);
        return taskMapper.toTaskResponse(saved);
    }

    @Override
    public TaskResponse getTaskById(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy task với id: " + taskId));
        workspaceAccessService.requireCurrentUserActiveMember(task.getProject().getWorkspace().getWorkspaceId());
        return taskMapper.toTaskResponse(task);
    }

    @Override
    public List<TaskResponse> getTasksByProjectId(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy project với id: " + projectId));
        workspaceAccessService.requireCurrentUserActiveMember(project.getWorkspace().getWorkspaceId());
        return taskRepository.findByProject_ProjectId(projectId).stream()
                .map(taskMapper::toTaskResponse)
                .toList();
    }

    @Override
    public List<TaskResponse> getTasksByListId(Long listId) {
        ProjectList list = projectListRepository.findById(listId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy list với id: " + listId));
        workspaceAccessService.requireCurrentUserActiveMember(list.getProject().getWorkspace().getWorkspaceId());
        return taskRepository.findByList_ListId(listId).stream()
                .map(taskMapper::toTaskResponse)
                .toList();
    }

    @Override
    public List<TaskResponse> getTasksByStatusId(Long statusId) {
        Status status = statusRepository.findById(statusId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy status với id: " + statusId));
        workspaceAccessService.requireCurrentUserActiveMember(status.getProject().getWorkspace().getWorkspaceId());
        return taskRepository.findByStatus_StatusId(statusId).stream()
                .map(taskMapper::toTaskResponse)
                .toList();
    }

    @Override
    @Transactional
    public TaskResponse updateTask(Long taskId, TaskUpdateRequest request) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy task với id: " + taskId));
        Long actorId = SecurityUtils.currentUserId();
        WorkspaceMember actor = workspaceAccessService.requireTaskActionAccess(
                task.getProject().getWorkspace().getWorkspaceId(),
                taskId,
                actorId);
        assertMemberTaskUpdateAllowed(actor, request);

        if (request.getTaskCode() != null && !request.getTaskCode().equals(task.getTaskCode())
                && taskRepository.existsByTaskCode(request.getTaskCode())) {
            throw new RuntimeException("Task code đã tồn tại: " + request.getTaskCode());
        }

        taskMapper.updateTask(task, request);

        Long currentProjectId = task.getProject().getProjectId();

        if (request.getListId() != null) {
            ProjectList list = projectListRepository.findById(request.getListId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy list với id: " + request.getListId()));
            if (!list.getProject().getProjectId().equals(currentProjectId)) {
                throw new RuntimeException("List " + request.getListId() + " không thuộc project " + currentProjectId);
            }
            task.setList(list);
        }

        if (request.getStatusId() != null) {
            Status status = statusRepository.findById(request.getStatusId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy status với id: " + request.getStatusId()));
            if (!status.getProject().getProjectId().equals(currentProjectId)) {
                throw new RuntimeException("Status " + request.getStatusId() + " không thuộc project " + currentProjectId);
            }
            if (!status.getList().getListId().equals(task.getList().getListId())) {
                throw new RuntimeException("Status " + request.getStatusId() + " không thuộc list " + task.getList().getListId());
            }
            task.setStatus(status);
        }

        if (request.getReporterId() != null) {
            User reporter = userRepository.findById(request.getReporterId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy reporter với id: " + request.getReporterId()));
            task.setReporter(reporter);
        }

        if (request.getParentTaskId() != null) {
            task.setParentTask(getValidParentTask(request.getParentTaskId(), currentProjectId, taskId));
        }

        if (!task.getStatus().getList().getListId().equals(task.getList().getListId())) {
            throw new RuntimeException("Status " + task.getStatus().getStatusId() + " không thuộc list " + task.getList().getListId());
        }

        task.setUpdatedAt(LocalDateTime.now());

        return taskMapper.toTaskResponse(taskRepository.save(task));
    }

    @Override
    @Transactional
    public void deleteTask(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy task với id: " + taskId));
        workspaceAccessService.requireCurrentUserOwnerAccess(task.getProject().getWorkspace().getWorkspaceId());
        taskRepository.deleteById(taskId);
    }

    private void assertMemberTaskUpdateAllowed(WorkspaceMember actor, TaskUpdateRequest request) {
        if (actor.getRoleInWorkspace() != WorkspaceMember.RoleInWorkspace.member) {
            return;
        }
        boolean touchesForbiddenField =
                request.getReporterId() != null
                        || request.getTaskCode() != null
                        || request.getTitle() != null
                        || request.getDescription() != null
                        || request.getTaskType() != null
                        || request.getPriority() != null
                        || request.getDueDate() != null
                        || request.getResolution() != null
                        || request.getTimeEstimateDays() != null
                        || request.getTimeSpentDays() != null
                        || request.getPoints() != null
                        || request.getPosition() != null
                        || request.getIsPrivate() != null
                        || request.getArchived() != null
                        || request.getParentTaskId() != null;
        if (touchesForbiddenField) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Member chỉ được cập nhật trạng thái hoặc di chuyển task trong project");
        }
    }

    private Task getValidParentTask(Long parentTaskId, Long projectId, Long currentTaskId) {
        if (parentTaskId == null) {
            return null;
        }

        Task parentTask = taskRepository.findById(parentTaskId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy parent task với id: " + parentTaskId));

        if (!parentTask.getProject().getProjectId().equals(projectId)) {
            throw new RuntimeException("Parent task " + parentTaskId + " không thuộc project " + projectId);
        }

        if (currentTaskId != null && parentTaskId.equals(currentTaskId)) {
            throw new RuntimeException("Task không thể là cha của chính nó");
        }

        return parentTask;
    }
}
