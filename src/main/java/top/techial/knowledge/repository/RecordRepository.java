package top.techial.knowledge.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import top.techial.knowledge.domain.Record;

import java.util.Collection;

/**
 * @author techial
 */
@Repository
public interface RecordRepository extends JpaRepository<Record, Integer>, QuerydslPredicateExecutor<Record> {

    @Transactional
    void deleteByNodeId(long id);

    @Transactional
    void deleteByUserId(int id);

    @Transactional
    void deleteByNodeIdIn(Collection<Long> ids);

    Page<Record> findAllByNodeId(long nodeId, Pageable pageable);
}
