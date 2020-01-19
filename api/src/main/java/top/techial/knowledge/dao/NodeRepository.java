package top.techial.knowledge.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import top.techial.knowledge.domain.Node;

/**
 * @author techial
 */
@Repository
public interface NodeRepository extends JpaRepository<Node, Long> {
}
