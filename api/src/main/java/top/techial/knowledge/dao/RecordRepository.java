package top.techial.knowledge.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import top.techial.knowledge.domain.Record;

import java.util.Collection;

/**
 * @author techial
 */
@Repository
public interface RecordRepository extends MongoRepository<Record, String> {

    Page<Record> findByNodeIdAndUserId(Long id, String userId, Pageable pageable);

    void deleteByNodeId(Long id);

    void deleteByNodeIdIn(Collection<Long> ids);

    void deleteByUserId(String id);
}
