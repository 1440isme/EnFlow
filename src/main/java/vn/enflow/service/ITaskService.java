package vn.enflow.service;

import vn.enflow.dto.request.TaskCreatetionRequest;
import vn.enflow.dto.request.TaskUpdateRequest;
import vn.enflow.dto.respone.TaskResponse;

import java.util.List;

public interface ITaskService {

    TaskResponse createtionTask(Long projectId, Long listId, Long statusId, TaskCreatetionRequest request);
    TaskResponse getTaskById(Long taskId);
    List<TaskResponse> getTasksByProjectId(Long projectId);
    List<TaskResponse> getTasksByListId(Long listId);
    List<TaskResponse> getTasksByStatusId(Long statusId);
    TaskResponse updateTask(Long taskId, TaskUpdateRequest request);
    void deleteTask(Long taskId);
}
