package top.techial.knowledge.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import top.techial.knowledge.domain.NodeRelationship;
import top.techial.knowledge.domain.NodeRelationshipPK;

/**
 * @author techial
 */
@Repository
public interface NodeRelationshipRepository extends JpaRepository<NodeRelationship, NodeRelationshipPK> {

    @Modifying
    @Query(
            nativeQuery = true,
            value = "insert into node_relationship (ancestor, descendant, distance)\n" +
                    "select n.ancestor, ?1, n.distance\n" +
                    "from node_relationship n where n.descendant = ?2 union all select ?1, ?1, 0;")
    @Transactional
    int insertNode(Long id, Long parentId);
}
