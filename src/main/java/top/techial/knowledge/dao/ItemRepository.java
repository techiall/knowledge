package top.techial.knowledge.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import top.techial.knowledge.domain.Item;

import java.util.List;

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
    void insert(Integer userId, Integer itemId);
}
