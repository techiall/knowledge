package top.techial.knowledge.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import top.techial.knowledge.domain.Node;

import java.util.Collection;

/**
 * @author techial
 */
@Repository
public interface NodeRepository extends JpaRepository<Node, Long> {
    @Transactional
    int deleteAllByItemId(Integer id);

    @Transactional
    int deleteByIdIn(Collection<Long> ids);

    @Transactional
    @Modifying
    @Query("update Node n set n.text = :text where n.id = :id")
    int saveText(Long id, String text);
}
