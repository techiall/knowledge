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

/**
 * @author techial
 */
@Service
@CacheConfig(cacheNames = "record")
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

    @Async
    @Cacheable(key = "#root.targetClass.simpleName + #root.methodName + #pageable", unless = "#result == null")
    public Page<Record> findAll(Pageable pageable) {
        return recordRepository.findAll(pageable);
    }

    @Async
    @Cacheable(key = "#root.targetClass.simpleName + #root.methodName + #id + #pageable", unless = "#result == null")
    public Page<Record> findByNodeId(Long id, Pageable pageable) {
        return recordRepository.findByNodeId(id, pageable);
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
}
