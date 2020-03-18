package top.techial.knowledge.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author techial
 */
@Configuration
@EnableJpaRepositories("top.techial.knowledge.repository")
@EnableJpaAuditing
@EnableTransactionManagement
@EnableElasticsearchRepositories("top.techial.knowledge.repository.search")
public class DatabaseConfiguration {
}
