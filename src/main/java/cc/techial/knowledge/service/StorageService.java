package cc.techial.knowledge.service;

import cc.techial.knowledge.domain.Storage;
import cc.techial.knowledge.repository.StorageRepository;
import cc.techial.knowledge.web.rest.errors.StorageFileNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author techial
 */
@Service
public class StorageService {
    private final StorageRepository storageRepository;

    public StorageService(StorageRepository storageRepository) {
        this.storageRepository = storageRepository;
    }

    @Transactional
    public void save(String sha1, MultipartFile file) {
        Storage storage = storageRepository.findById(sha1).orElse(new Storage());
        storage.setId(sha1);
        storage.setContentType(file.getContentType());
        storage.setOriginalFilename(file.getOriginalFilename());
        storageRepository.save(storage);
    }

    public Storage findById(String id) {
        return storageRepository.findById(id).orElseThrow(StorageFileNotFoundException::new);
    }
}
