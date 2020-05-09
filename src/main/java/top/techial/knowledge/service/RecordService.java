package top.techial.knowledge.service;

import org.springframework.stereotype.Service;
import top.techial.knowledge.domain.Record;
import top.techial.knowledge.repository.RecordRepository;
import top.techial.knowledge.repository.UserRepository;

/**
 * @author techial
 */
@Service
public class RecordService {
    private final RecordRepository recordRepository;
    private final UserRepository userRepository;

    public RecordService(RecordRepository recordRepository, UserRepository userRepository) {
        this.recordRepository = recordRepository;
        this.userRepository = userRepository;
    }

    public void save(Long nodeId, Integer userId, String operatorMessage, String message) {
        var record = new Record()
                .setNodeId(nodeId)
                .setOperator(operatorMessage)
                .setMessage(message)
                .setUser(userRepository.getOne(userId));
        recordRepository.save(record);
    }

}
