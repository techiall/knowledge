package top.techial.knowledge.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.neo4j.annotation.EnableNeo4jAuditing;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;

/**
 * @author techial
 */
@EnableNeo4jRepositories(basePackages = "top.techial.knowledge.dao")
@EnableNeo4jAuditing
@Configuration
public class Neo4jConfig {
}
