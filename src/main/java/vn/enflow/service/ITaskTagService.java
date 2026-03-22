package vn.enflow.service;

import vn.enflow.dto.request.TaskTagRequest;
import vn.enflow.dto.respone.TaskTagResponse;

import java.util.List;

public interface ITaskTagService {
    TaskTagResponse addTagToTask(Long taskId, TaskTagRequest request);
    List<TaskTagResponse> getTagsByTaskId(Long taskId);
    List<TaskTagResponse> getTasksByTagId(Long tagId);
    void removeTagFromTask(Long taskId, Long tagId);
}
