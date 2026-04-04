package vn.enflow.config;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import vn.enflow.repository.WorkspaceMemberRepository;

/**
 * Đổi bản ghi cũ {@code admin} → {@code member} trước khi ứng dụng phục vụ request (enum Java không còn admin).
 */
@Component
@Order(0)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WorkspaceLegacyRoleMigrationRunner implements InitializingBean {

    WorkspaceMemberRepository workspaceMemberRepository;

    @Override
    public void afterPropertiesSet() {
        workspaceMemberRepository.migrateLegacyAdminRoles();
    }
}
