package top.techial.knowledge.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import top.techial.knowledge.domain.Node;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * @author techial
 */
@Repository
public interface NodeRepository extends JpaRepository<Node, Long>, QuerydslPredicateExecutor<Node> {
    @Transactional
    void deleteAllByItemId(int id);

    @Query("select n from Node n inner join Item i on n.itemId = i.id where n.itemId = ?2 and n.name = ?1")
    Optional<Node> findByItemIdAndName(String name, int itemId);

    @Transactional
    void deleteByIdIn(Collection<Long> ids);

    @Query("select n.id from Node n where n.itemId in :ids")
    List<Long> findByItemIdIn(Collection<Integer> ids);

    @Query("select n from Node n inner join NodeRelationship nr on n.id = nr.descendant where nr.ancestor = ?1")
    List<Node> findChildNode(long id);

    @Query("select n from Node n inner join NodeRelationship nr on n.id = nr.ancestor where nr.descendant = ?1")
    List<Node> findParentNode(long id);

    @Query("select i.rootNode.id from Node n inner join Item i on n.itemId = i.id where n.id = ?1")
    Long findItemRootNodeId(long id);
}
