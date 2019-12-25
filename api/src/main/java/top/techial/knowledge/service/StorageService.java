package top.techial.knowledge.service;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import top.techial.knowledge.dao.StorageRepository;
import top.techial.knowledge.domain.KnowledgeNode;
import top.techial.knowledge.domain.Storage;
import top.techial.knowledge.service.storage.FileStorageService;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

/**
 * @author techial
 */
@Service
@Log4j2
public class StorageService {
    private final StorageRepository storageRepository;
    private final FileStorageService fileStorageService;
    private final KnowledgeNodeService knowledgeNodeService;

    public StorageService(StorageRepository storageRepository, FileStorageService fileStorageService, KnowledgeNodeService knowledgeNodeService) {
        this.storageRepository = storageRepository;
        this.fileStorageService = fileStorageService;
        this.knowledgeNodeService = knowledgeNodeService;
    }

    public String saveNodeStorage(String resource, Long nodeId) throws IOException {
        String fileName = RandomStringUtils.randomAlphanumeric(10);
        MultipartFile file = new MockMultipartFile(fileName, new ByteArrayInputStream(resource.getBytes()));
        String sha1 = fileStorageService.store(file);

        Storage storage = new Storage().setSha1(sha1).setType("text/plain").setFileName(fileName);
        storageRepository.save(storage);

        KnowledgeNode node = knowledgeNodeService.findById(nodeId).setResourceId(sha1);
        knowledgeNodeService.save(node);

        return sha1;
    }

    public String findByNodeId(Long id) throws IOException {
        String resourceId = knowledgeNodeService.findById(id).getResourceId();
        Resource resource = fileStorageService.loadAsResource(resourceId);
        return new BufferedReader(new InputStreamReader(resource.getInputStream()))
            .lines().collect(Collectors.joining("\n"));
    }

    public Storage findBySha1(String id) {
        return storageRepository.findFirstBySha1(id)
            .orElseThrow(() -> new IllegalArgumentException(String.format("sha1 %s not found.", id)));
    }
}
