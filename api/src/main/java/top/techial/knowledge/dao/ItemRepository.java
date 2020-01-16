package top.techial.knowledge.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import top.techial.knowledge.domain.Item;

/**
 * @author techial
 */
@Repository
public interface ItemRepository extends JpaRepository<Item, Integer> {
}
