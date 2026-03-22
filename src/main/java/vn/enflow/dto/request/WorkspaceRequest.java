package vn.enflow.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkspaceRequest {
    private String name;
    private String workspaceKey;
    private String description;
    private Long ownerUserId;
    private Boolean isPrivate;
}
