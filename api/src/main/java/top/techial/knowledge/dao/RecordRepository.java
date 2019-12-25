package top.techial.knowledge.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;
import top.techial.knowledge.domain.Record;

import java.util.Collection;

/**
 * @author techial
 */
@Repository
public interface RecordRepository extends JpaRepository<Record, Integer>, QuerydslPredicateExecutor<Record> {

    void deleteByNodeId(Long id);

    void deleteByUsersIdIn(Integer id);

    void deleteByNodeIdIn(Collection<Long> ids);

}
