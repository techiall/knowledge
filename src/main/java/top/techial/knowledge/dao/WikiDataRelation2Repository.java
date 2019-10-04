package top.techial.knowledge.dao;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;
import top.techial.knowledge.domain.WikiDataRelation2;

/**
 * @author techial
 */
@Repository
public interface WikiDataRelation2Repository extends Neo4jRepository<WikiDataRelation2, Long> {
}
