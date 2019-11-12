package top.techial.knowledge.dao;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;
import top.techial.knowledge.domain.ParentChildRelation;

import java.util.List;
import java.util.Optional;

/**
 * @author techial
 */
@Repository
public interface ParentChildRelationRepository extends Neo4jRepository<ParentChildRelation, Long> {
    /**
     * findFirstByStartNodeNameAndEndNodeName
     *
     * @param start id
     * @param end   id
     * @return relation
     */
    Optional<ParentChildRelation> findFirstByStartNodeNameAndEndNodeName(String start, String end);

    /**
     * find by node name
     *
     * @param name
     * @return
     */
    List<ParentChildRelation> findByStartNodeName(String name);
}
