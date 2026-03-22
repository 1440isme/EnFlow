package vn.enflow.service.impl;

import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import vn.enflow.dto.request.TaskTagRequest;
import vn.enflow.dto.respone.TaskTagResponse;
import vn.enflow.entity.Tag;
import vn.enflow.entity.Task;
import vn.enflow.entity.TaskTag;
import vn.enflow.entity.TaskTagId;
import vn.enflow.mapper.TaskTagMapper;
import vn.enflow.repository.TagRepository;
import vn.enflow.repository.TaskRepository;
import vn.enflow.repository.TaskTagRepository;
import vn.enflow.service.ITaskTagService;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TaskTagServiceImpl implements ITaskTagService {

    TaskTagRepository taskTagRepository;
    TaskRepository taskRepository;
    TagRepository tagRepository;
    TaskTagMapper taskTagMapper;

    @Override
    @Transactional
    public TaskTagResponse addTagToTask(Long taskId, TaskTagRequest request) {
        if (request.getTagId() == null) {
            throw new RuntimeException("tagId là bắt buộc");
        }

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy task với id: " + taskId));

        Tag tag = tagRepository.findById(request.getTagId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy tag với id: " + request.getTagId()));

        TaskTagId taskTagId = new TaskTagId(taskId, request.getTagId());
        if (taskTagRepository.existsById(taskTagId)) {
            throw new RuntimeException("Tag đã được gắn vào task này");
        }

        TaskTag taskTag = TaskTag.builder()
                .id(taskTagId)
                .task(task)
                .tag(tag)
                .build();

        return taskTagMapper.toTaskTagResponse(taskTagRepository.save(taskTag));
    }

    @Override
    public List<TaskTagResponse> getTagsByTaskId(Long taskId) {
        if (!taskRepository.existsById(taskId)) {
            throw new RuntimeException("Không tìm thấy task với id: " + taskId);
        }

        return taskTagRepository.findById_TaskId(taskId).stream()
                .map(taskTagMapper::toTaskTagResponse)
                .toList();
    }

    @Override
    public List<TaskTagResponse> getTasksByTagId(Long tagId) {
        if (!tagRepository.existsById(tagId)) {
            throw new RuntimeException("Không tìm thấy tag với id: " + tagId);
        }

        return taskTagRepository.findById_TagId(tagId).stream()
                .map(taskTagMapper::toTaskTagResponse)
                .toList();
    }

    @Override
    @Transactional
    public void removeTagFromTask(Long taskId, Long tagId) {
        TaskTagId taskTagId = new TaskTagId(taskId, tagId);
        if (!taskTagRepository.existsById(taskTagId)) {
            throw new RuntimeException("Không tìm thấy liên kết task-tag");
        }

        taskTagRepository.deleteById(taskTagId);
    }
}
