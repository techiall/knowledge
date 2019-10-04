package top.techial.knowledge.dao;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;
import top.techial.knowledge.domain.WikiDataRelation;

/**
 * @author techial
 */
@Repository
public interface WikiDataRelationRepository extends Neo4jRepository<WikiDataRelation, Long> {
}
