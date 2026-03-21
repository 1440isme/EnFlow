package vn.enflow.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkspaceMemberId implements Serializable {

    @Column(name = "workspace_id")
    Long workspaceId;

    @Column(name = "user_id")
    Long userId;
}
