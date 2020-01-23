package top.techial.knowledge.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import top.techial.knowledge.domain.Item;

import java.util.List;
import java.util.Optional;

/**
 * @author techial
 */
@Repository
public interface ItemRepository extends JpaRepository<Item, Integer> {

    List<Item> findAllByAuthorId(Integer id);

    void deleteByAuthorId(Integer id);

    @Modifying
    @Query(
            nativeQuery = true,
            value = "insert into user_item (user_id, item_id) values (?1, ?2);"
    )
    @Transactional
    void insert(Integer userId, Integer itemId);

    @Query("select i.rootNode.id from Item i where i.id = :id")
    Optional<Long> findRootNodeId(Integer id);
}
