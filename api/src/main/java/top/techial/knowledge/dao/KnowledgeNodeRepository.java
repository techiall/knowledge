package top.techial.knowledge.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;
import top.techial.knowledge.domain.KnowledgeNode;

import java.util.Optional;

/**
 * @author techial
 */
@Repository
public interface KnowledgeNodeRepository extends Neo4jRepository<KnowledgeNode, Long> {
    Page<KnowledgeNode> findByNameLikeAndUserId(String name, Integer userId, Pageable pageable);

    Page<KnowledgeNode> findByUserIdAndParentNodeIdIsNull(Integer userId, Pageable pageable, int depth);

    Optional<KnowledgeNode> findFirstByName(String name);

    void deleteByUserId(Integer userId);
}
