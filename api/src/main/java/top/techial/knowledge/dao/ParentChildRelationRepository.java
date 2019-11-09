package top.techial.knowledge.dao;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;
import top.techial.knowledge.domain.ParentChildRelation;

import java.util.Optional;

/**
 * @author techial
 */
@Repository
public interface ParentChildRelationRepository extends Neo4jRepository<ParentChildRelation, Long> {
    /**
     * findByStartNodeIdAndEndNodeId
     *
     * @param startId id
     * @param endId   id
     * @return relation
     */
    Optional<ParentChildRelation> findFirstByStartNodeIdAndEndNodeId(Long startId, Long endId);
}
