package top.techial.knowledge.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import top.techial.knowledge.domain.NodeRelationship;
import top.techial.knowledge.domain.NodeRelationshipPK;

import java.util.Collection;

/**
 * @author techial
 */
@Repository
public interface NodeRelationshipRepository extends JpaRepository<NodeRelationship, NodeRelationshipPK> {

    @Modifying
    @Query(
            nativeQuery = true,
            value = "insert into node_relationship (ancestor, descendant, distance, create_time, update_time)\n" +
                    "select n.ancestor, ?1, n.distance + 1, now(), now()\n" +
                    "from node_relationship n where n.descendant = ?2 union all select ?1, ?1, 0, now(), now()")
    @Transactional
    int insertNode(Long id, Long parentId);

    @Modifying
    @Query(
            nativeQuery = true,
            value = "insert into node_relationship (ancestor, descendant, distance, create_time, update_time)\n" +
                    "value (?1, ?1, 0, now(), now())")
    @Transactional
    int insertNode(Long id);

    @Modifying
    @Query(
            nativeQuery = true,
            value = "delete n from node_relationship n\n" +
                    "join node_relationship m on (n.descendant = m.descendant)\n" +
                    "where n.ancestor = ?1"
    )
    @Transactional
    int deleteByNodeId(Long id);

    @Modifying
    @Query(
            nativeQuery = true,
            value = "delete n from node_relationship n\n" +
                    "join node_relationship m on (n.descendant = m.descendant)\n" +
                    "where n.ancestor in ?1"
    )
    @Transactional
    int deleteByNodeIdIn(Collection<Long> ids);
}
