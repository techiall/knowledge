package top.techial.knowledge.dao;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;
import top.techial.knowledge.domain.HuDongItem;

/**
 * @author techial
 */
@Repository
public interface HuDongItemRepository extends Neo4jRepository<HuDongItem, Long> {
}
