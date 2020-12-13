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

    public RecordService(RecordRepository recordRepository,
                         UserRepository userRepository) {
        this.recordRepository = recordRepository;
        this.userRepository = userRepository;
    }

    public void save(long nodeId, int userId, String operatorMessage, String message) {
        final var record = new Record();
        record.setNodeId(nodeId);
        record.setOperator(operatorMessage);
        record.setMessage(message);
        record.setUser(userRepository.getOne(userId));
        recordRepository.save(record);
    }
}
