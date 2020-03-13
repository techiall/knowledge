package top.techial.knowledge.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import top.techial.knowledge.domain.Storage;
import top.techial.knowledge.repository.StorageRepository;
import top.techial.knowledge.web.rest.errors.StorageFileNotFoundException;

/**
 * @author techial
 */
@Service
@Log4j2
@CacheConfig(cacheNames = "common")
public class StorageService {
    private final StorageRepository storageRepository;

    public StorageService(StorageRepository storageRepository) {
        this.storageRepository = storageRepository;
    }

    @CacheEvict(allEntries = true)
    @Transactional
    public void save(String sha1, MultipartFile file) {
        Storage storage = storageRepository.findById(sha1).orElse(new Storage());
        storage = storage
                .setId(sha1)
                .setContentType(file.getContentType())
                .setOriginalFilename(file.getOriginalFilename());
        storageRepository.save(storage);
    }

    @Cacheable(key = "#root.targetClass.simpleName + #root.methodName + #p0", unless = "#result == null")
    public Storage findById(String id) {
        return storageRepository.findById(id).orElseThrow(StorageFileNotFoundException::new);
    }
}
