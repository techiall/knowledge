package top.techial.knowledge.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import top.techial.knowledge.dao.StorageRepository;
import top.techial.knowledge.domain.Storage;

/**
 * @author techial
 */
@Service
@Log4j2
public class StorageService {
    private final StorageRepository storageRepository;

    public StorageService(StorageRepository storageRepository) {
        this.storageRepository = storageRepository;
    }

    public Storage findBySha1(String id) {
        return storageRepository.findFirstBySha1(id)
            .orElseThrow(() -> new IllegalArgumentException(String.format("sha1 %s not found.", id)));
    }
}
