package top.techial.knowledge.aop.logging;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import top.techial.knowledge.config.ProfileConstants;

/**
 * @author techial
 */
@Configuration
public class LoggingAspectConfiguration {

    @Bean
    @Profile({ProfileConstants.DEV, ProfileConstants.TEST})
    public LoggingAspect loggingAspect() {
        return new LoggingAspect();
    }
}
