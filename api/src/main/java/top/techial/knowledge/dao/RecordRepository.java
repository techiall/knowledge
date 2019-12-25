package top.techial.knowledge.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import top.techial.knowledge.domain.Record;

import java.util.Collection;

/**
 * @author techial
 */
@Repository
public interface RecordRepository extends JpaRepository<Record, Integer> {

    Page<Record> findByNodeIdAndUserId(Long id, Integer userId, Pageable pageable);

    void deleteByNodeId(Long id);

    void deleteByNodeIdIn(Collection<Long> ids);

    void deleteByUserId(Integer id);
}
