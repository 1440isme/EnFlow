package vn.enflow.service;

import vn.enflow.dto.request.StatusesCreatetionRequest;
import vn.enflow.dto.request.StatusesUpdateRequest;
import vn.enflow.dto.respone.StatusesRespone;

import java.util.List;

public interface IStatusesService {

    StatusesRespone createtionStatus(Long projectId, Long listId, StatusesCreatetionRequest request);
    StatusesRespone getStatusById(Long statusId);
    List<StatusesRespone> getStatusesByProjectId(Long projectId);
    List<StatusesRespone> getStatusesByListId(Long listId);
    StatusesRespone updateStatus(Long statusId, StatusesUpdateRequest updateRequest);
    void deleteStatus(Long statusId);
}
