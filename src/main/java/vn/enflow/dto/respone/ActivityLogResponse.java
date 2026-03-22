package vn.enflow.dto.respone;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ActivityLogResponse {
    Long logId;
    
    Long workspaceId;
    
    Long projectId;
    
    Long taskId;
    
    Long actorId;
    
    String action;
    
    String targetType;
    
    Long targetId;
    
    String metaJson;
    
    LocalDateTime createdAt;
}
