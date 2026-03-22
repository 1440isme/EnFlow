package vn.enflow.service.impl;

import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import vn.enflow.dto.request.TagCreatetionRequest;
import vn.enflow.dto.request.TagUpdateRequest;
import vn.enflow.dto.respone.TagResponse;
import vn.enflow.entity.Tag;
import vn.enflow.entity.Workspace;
import vn.enflow.mapper.TagMapper;
import vn.enflow.repository.TagRepository;
import vn.enflow.repository.WorkspaceRepository;
import vn.enflow.service.ITagService;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TagServiceImpl implements ITagService {

    TagRepository tagRepository;
    WorkspaceRepository workspaceRepository;
    TagMapper tagMapper;

    @Override
    @Transactional
    public TagResponse createtionTag(Long workspaceId, TagCreatetionRequest request) {
        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy workspace với id: " + workspaceId));

        Tag tag = tagMapper.toTag(request);
        tag.setWorkspace(workspace);

        Tag saved = tagRepository.save(tag);
        return tagMapper.toTagResponse(saved);
    }

    @Override
    public TagResponse getTagById(Long tagId) {
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy tag với id: " + tagId));
        return tagMapper.toTagResponse(tag);
    }

    @Override
    public List<TagResponse> getTagsByWorkspaceId(Long workspaceId) {
        if (!workspaceRepository.existsById(workspaceId)) {
            throw new RuntimeException("Không tìm thấy workspace với id: " + workspaceId);
        }

        return tagRepository.findByWorkspace_WorkspaceId(workspaceId).stream()
                .map(tagMapper::toTagResponse)
                .toList();
    }

    @Override
    @Transactional
    public TagResponse updateTag(Long tagId, TagUpdateRequest request) {
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy tag với id: " + tagId));

        tagMapper.updateTag(tag, request);
        return tagMapper.toTagResponse(tagRepository.save(tag));
    }

    @Override
    @Transactional
    public void deleteTag(Long tagId) {
        if (!tagRepository.existsById(tagId)) {
            throw new RuntimeException("Không tìm thấy tag với id: " + tagId);
        }
        tagRepository.deleteById(tagId);
    }
}
