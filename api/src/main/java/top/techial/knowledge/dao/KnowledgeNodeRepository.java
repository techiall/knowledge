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
    Page<KnowledgeNode> findByNameLikeAndItemId(String name, Integer itemId, Pageable pageable);

    Page<KnowledgeNode> findAllByNameLike(String name, Pageable pageable);

    Page<KnowledgeNode> findByItemIdAndParentNodeIdIsNull(Integer itemId, Pageable pageable);

    Optional<KnowledgeNode> findFirstByName(String name);

    Long deleteByItemId(Integer itemId);
}
