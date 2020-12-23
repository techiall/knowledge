package cc.techial.knowledge.aop.authority;

import cc.techial.knowledge.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author techial
 */
@Configuration
public class FlushAuthorityAspectConfiguration {

    @Bean
    public FlushAuthorityAspect flushAuthorityAspect(UserService userService) {
        return new FlushAuthorityAspect(userService);
    }
}
