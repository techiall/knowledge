package top.techial.knowledge.dao;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;
import top.techial.knowledge.domain.NewNode;

import java.util.Optional;

/**
 * @author techial
 */
@Repository
public interface NewNodeRepository extends Neo4jRepository<NewNode, Long> {
    /**
     * find by title
     *
     * @param title
     * @return
     */
    Optional<NewNode> findFirstByTitle(String title);
}
