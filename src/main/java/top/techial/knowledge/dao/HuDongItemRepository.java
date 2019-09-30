package top.techial.knowledge.dao;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;
import top.techial.knowledge.domain.HuDongItem;

import java.util.Optional;

/**
 * @author techial
 */
@Repository
public interface HuDongItemRepository extends Neo4jRepository<HuDongItem, Long> {
    /**
     * find by title
     *
     * @param title
     * @return
     */
    Optional<HuDongItem> findFirstByTitle(String title);
}
