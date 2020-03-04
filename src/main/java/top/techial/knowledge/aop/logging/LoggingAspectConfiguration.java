package top.techial.knowledge.aop.logging;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import top.techial.knowledge.config.ConstantsProfile;

/**
 * @author techial
 */
@Configuration
public class LoggingAspectConfiguration {

    @Bean
    @Profile({ConstantsProfile.DEV, ConstantsProfile.TEST})
    public LoggingAspect loggingAspect() {
        return new LoggingAspect();
    }
}
