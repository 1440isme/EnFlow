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
public class AttachmentResponse {
    Long attachmentId;
    
    Long taskId;
    
    Long uploadedBy;
    
    String fileName;
    
    String fileUrl;
    
    String mimeType;
    
    LocalDateTime createdAt;
}
