package top.techial.knowledge.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import top.techial.knowledge.dao.RecordRepository;
import top.techial.knowledge.domain.Record;

/**
 * @author techial
 */
@Service
public class RecordService {
    private final RecordRepository recordRepository;

    public RecordService(RecordRepository recordRepository) {
        this.recordRepository = recordRepository;
    }

    public Record save(Record record) {
        return recordRepository.save(record);
    }

    public Page<Record> findAll(Pageable pageable) {
        return recordRepository.findAll(pageable);
    }

    public Page<Record> findByNodeId(Long id, Pageable pageable) {
        return recordRepository.findByNodeId(id, pageable);
    }

    public void deleteByNodeId(Long id) {
        recordRepository.deleteByNodeId(id);
    }
}
