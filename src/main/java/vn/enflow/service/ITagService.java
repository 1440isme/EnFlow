package vn.enflow.service;

import vn.enflow.dto.request.TagCreatetionRequest;
import vn.enflow.dto.request.TagUpdateRequest;
import vn.enflow.dto.respone.TagResponse;

import java.util.List;

public interface ITagService {
    TagResponse createtionTag(Long workspaceId, TagCreatetionRequest request);
    TagResponse getTagById(Long tagId);
    List<TagResponse> getTagsByWorkspaceId(Long workspaceId);
    TagResponse updateTag(Long tagId, TagUpdateRequest request);
    void deleteTag(Long tagId);
}
