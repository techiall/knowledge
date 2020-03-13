package top.techial.knowledge.service;

import lombok.SneakyThrows;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.techial.knowledge.domain.Record;
import top.techial.knowledge.domain.User;
import top.techial.knowledge.repository.RecordRepository;

/**
 * @author techial
 */
@Service
@CacheConfig(cacheNames = "common")
public class RecordService {
    private final RecordRepository recordRepository;

    public RecordService(RecordRepository recordRepository) {
        this.recordRepository = recordRepository;
    }

    @SneakyThrows
    @Async
    @CacheEvict(allEntries = true)
    @Transactional
    public void save(Long nodeId, Integer userId, String operatorMessage, String message) {
        Record record = new Record()
                .setNodeId(nodeId)
                .setOperator(operatorMessage)
                .setMessage(message)
                .setUser(new User().setId(userId));
        recordRepository.save(record);
    }

}
