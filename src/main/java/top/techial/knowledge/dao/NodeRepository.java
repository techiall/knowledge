package top.techial.knowledge.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
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
public interface NodeRepository extends JpaRepository<Node, Long> {
    @Transactional
    void deleteAllByItemId(Integer id);


    @Query("select n from Node n inner join Item i on n.itemId = ?2 where n.name = ?1")
    Optional<Node> findByItemIdAndName(String name, Integer itemId);

    @Transactional
    void deleteByIdIn(Collection<Long> ids);

    @Transactional
    @Modifying
    @Query("update Node n set n.text = :text where n.id = :id")
    void saveText(Long id, String text);

    @Query("select n.id from Node n where n.itemId in :ids")
    List<Long> findByItemIdIn(Collection<Integer> ids);

    @Query("select n.text from Node n where n.id = ?1")
    String findTextById(Long id);

    @Query("select n from Node n inner join NodeRelationship nr on n.id = nr.descendant where nr.ancestor = ?1")
    List<Node> findChildNode(Long id);

    @Query("select n from Node n inner join NodeRelationship nr on n.id = nr.ancestor where nr.descendant = ?1")
    List<Node> findParentNode(Long id);

    @Query("select i.rootNode.id from Node n inner join Item i on n.itemId = i.id where n.id = ?1")
    Long findItemRootNodeId(Long id);
}
