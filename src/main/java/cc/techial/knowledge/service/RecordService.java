package cc.techial.knowledge.service;

import cc.techial.knowledge.domain.Record;
import cc.techial.knowledge.repository.RecordRepository;
import cc.techial.knowledge.repository.UserRepository;
import org.springframework.stereotype.Service;

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
