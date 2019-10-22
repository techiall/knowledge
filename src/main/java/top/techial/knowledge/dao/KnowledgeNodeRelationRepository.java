package top.techial.knowledge.dao;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;
import top.techial.knowledge.domain.KnowledgeNodeRelation;

import java.util.List;

/**
 * @author techial
 */
@Repository
public interface KnowledgeNodeRelationRepository extends Neo4jRepository<KnowledgeNodeRelation, Long> {

    /**
     * findFirstByStartNodeName
     *
     * @param name
     * @return
     */
    List<KnowledgeNodeRelation> findFirstByStartNodeName(String name);

}
