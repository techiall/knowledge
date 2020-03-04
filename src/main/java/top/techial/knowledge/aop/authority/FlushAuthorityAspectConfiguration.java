package top.techial.knowledge.aop.authority;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.techial.knowledge.service.UserService;

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
