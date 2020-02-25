package top.techial.knowledge.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author techial
 */
@EnableJpaRepositories(basePackages = "top.techial.knowledge.repository")
@EnableJpaAuditing
@Configuration
@EnableTransactionManagement
public class JpaConfig {
}
