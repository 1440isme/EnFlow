package vn.enflow.dto.respone;

import java.time.LocalDateTime;

public class ProjectListResponse {
    Long listProjectId;
    String name;
    String description;
    Integer position;
    Boolean isPrivate;
    Boolean archived;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    Long projectId;

}
