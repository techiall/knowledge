package top.techial.knowledge.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import top.techial.knowledge.dao.StorageRepository;
import top.techial.knowledge.domain.Storage;
import top.techial.knowledge.exception.StorageFileNotFoundException;

/**
 * @author techial
 */
@Service
@Log4j2
@CacheConfig(cacheNames = "user")
public class StorageService {
    private final StorageRepository storageRepository;

    public StorageService(StorageRepository storageRepository) {
        this.storageRepository = storageRepository;
    }

    @CacheEvict(allEntries = true)
    @Transactional
    public Storage save(String sha1, MultipartFile file) {
        Storage storage = storageRepository.findFirstBySha1(sha1).orElse(new Storage());
        storage = storage
            .setSha1(sha1)
            .setContentType(file.getContentType())
            .setOriginalFilename(file.getOriginalFilename());
        return storageRepository.save(storage);
    }

    @CacheEvict(allEntries = true)
    @Async
    @Transactional
    public void deleteBySHA1(String id) {
        storageRepository.deleteBySha1(id);
    }

    @Cacheable(key = "#root.targetClass.simpleName + #root.methodName + #p0", unless = "#result == null")
    public Storage findBySHA1(String id) {
        return storageRepository.findFirstBySha1(id)
            .orElseThrow(() -> new StorageFileNotFoundException(String.format("SHA1: [%s] not found.", id)));
    }
}
