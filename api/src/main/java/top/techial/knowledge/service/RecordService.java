package top.techial.knowledge.service;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.techial.knowledge.dao.RecordRepository;
import top.techial.knowledge.domain.OperatorMessageEnum;
import top.techial.knowledge.domain.Record;
import top.techial.knowledge.domain.User;

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
    public Record save(Long nodeId, Integer userId, OperatorMessageEnum operatorMessageEnum, String message) {
        Record record = new Record()
            .setNodeId(nodeId)
            .setOperator(operatorMessageEnum)
            .setMessage(message)
            .setUser(new User().setId(userId));
        return recordRepository.save(record);
    }

    @CacheEvict(allEntries = true)
    @Transactional
    public Record save(Record record) {
        return recordRepository.save(record);
    }

    @Cacheable(key = "#root.targetClass.simpleName + #root.methodName + #nodeId + #pageable", unless = "#result == null")
    public Page<Record> findByNodeId(Long nodeId, Pageable pageable) {
        return recordRepository.findAllByNodeId(nodeId, pageable);
    }

    @CacheEvict(allEntries = true)
    @Async
    @Transactional
    public void deleteByNodeId(Long id) {
        recordRepository.deleteByNodeId(id);
    }

    @CacheEvict(allEntries = true)
    @Async
    @Transactional
    public void deleteByNodeIds(Set<Long> ids) {
        recordRepository.deleteByNodeIdIn(ids);
    }

    @CacheEvict(allEntries = true)
    @Async
    @Transactional
    public void deleteByUserId(Integer id) {
        recordRepository.deleteByUserId(id);
    }
}
