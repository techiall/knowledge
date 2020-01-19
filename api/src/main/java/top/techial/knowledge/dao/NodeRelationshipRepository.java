package top.techial.knowledge.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import top.techial.knowledge.domain.NodeRelationship;
import top.techial.knowledge.domain.NodeRelationshipPK;

/**
 * @author techial
 */
@Repository
public interface NodeRelationshipRepository extends JpaRepository<NodeRelationship, NodeRelationshipPK> {
}
