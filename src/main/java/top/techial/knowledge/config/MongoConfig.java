package top.techial.knowledge.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * @author techial
 */
@EnableMongoRepositories(basePackages = "top.techial.knowledge.dao")
@EnableMongoAuditing
@Configuration
public class MongoConfig {
}
