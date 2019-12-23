package top.techial.knowledge.service;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import top.techial.knowledge.dao.RecordRepository;
import top.techial.knowledge.domain.Record;

import java.util.Set;

/**
 * @author techial
 */
@Service
@CacheConfig(cacheNames = {"user", "record", "node-relation", "knowledge-node"})
public class RecordService {
    private final RecordRepository recordRepository;

    public RecordService(RecordRepository recordRepository) {
        this.recordRepository = recordRepository;
    }

    @Async
    @CacheEvict(allEntries = true)
    public Record save(Record record) {
        return recordRepository.save(record);
    }

    @Cacheable(key = "#root.targetClass.simpleName + #root.methodName + #pageable", unless = "#result == null")
    public Page<Record> findAll(Pageable pageable) {
        return recordRepository.findAll(pageable);
    }

    @Cacheable(key = "#root.targetClass.simpleName + #root.methodName + #id + #pageable", unless = "#result == null")
    public Page<Record> findByNodeId(Long id, String userId, Pageable pageable) {
        return recordRepository.findByNodeIdAndUserId(id, userId, pageable);
    }

    @CacheEvict(allEntries = true)
    @Async
    public void deleteByNodeId(Long id) {
        recordRepository.deleteByNodeId(id);
    }

    @Async
    @CacheEvict(allEntries = true)
    public void deleteAll() {
        recordRepository.deleteAll();
    }

    @CacheEvict(allEntries = true)
    @Async
    public void deleteByNodeIds(Set<Long> ids) {
        recordRepository.deleteByNodeIdIn(ids);
    }
}
