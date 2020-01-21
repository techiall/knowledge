package top.techial.knowledge.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * @author techial
 */
@EnableJpaRepositories(basePackages = "top.techial.knowledge.dao")
@EnableJpaAuditing
@Configuration
public class JpaConfig {
}
