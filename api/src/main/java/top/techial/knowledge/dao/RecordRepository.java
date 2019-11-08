package top.techial.knowledge.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import top.techial.knowledge.domain.Record;

/**
 * @author techial
 */
@Repository
public interface RecordRepository extends MongoRepository<Record, String> {

    /**
     * find by node id
     *
     * @param id       id
     * @param pageable pageable
     * @return page
     */
    Page<Record> findByNodeId(Long id, Pageable pageable);
}
