package top.techial.knowledge.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;
import top.techial.knowledge.domain.KnowledgeNode;

/**
 * @author techial
 */
@Repository
public interface KnowledgeNodeRepository extends Neo4jRepository<KnowledgeNode, Long> {
    /**
     * find by name
     *
     * @param name
     * @return
     */
    Page<KnowledgeNode> findByNameLike(String name, Pageable pageable);
}
