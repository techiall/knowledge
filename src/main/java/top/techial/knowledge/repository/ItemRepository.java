package top.techial.knowledge.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import top.techial.knowledge.domain.Item;

import java.util.List;
import java.util.Optional;

/**
 * @author techial
 */
@Repository
public interface ItemRepository extends JpaRepository<Item, Integer>, QuerydslPredicateExecutor<Item> {

    Page<Item> findAllByAuthorId(Integer id, Pageable pageable);

    List<Item> findAllByAuthorId(Integer id);

    @EntityGraph(attributePaths = "author")
    Page<Item> findByShare(Boolean share, Pageable pageable);

    List<Item> findByShare(Boolean share);

    void deleteByAuthorId(Integer id);

    @Modifying
    @Query(
            nativeQuery = true,
            value = "insert into user_item (user_id, item_id) values (?1, ?2);"
    )
    @Transactional
    void insert(Integer userId, Integer itemId);

    @Modifying
    @Query(
            nativeQuery = true,
            value = "delete from user_item where user_id = :userId and item_id = :itemId"
    )
    @Transactional
    void deleteTmpByUserIdAndItemId(Integer userId, Integer itemId);

    @Query("select i.rootNode.id from Item i where i.id = :id")
    Optional<Long> findRootNodeId(Integer id);

    @Query("select i.rootNode.id from Item i inner join Node n on n.itemId = i.id where n.id = :id")
    Optional<Integer> findItemIdByRootId(Long id);
}
