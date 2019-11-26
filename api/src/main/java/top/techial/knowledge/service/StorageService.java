package top.techial.knowledge.service;

import com.mongodb.client.gridfs.model.GridFSFile;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;
import top.techial.knowledge.domain.KnowledgeNode;

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
    private final GridFsTemplate gridFsTemplate;
    private final KnowledgeNodeService knowledgeNodeService;

    public StorageService(GridFsTemplate gridFsTemplate, KnowledgeNodeService knowledgeNodeService) {
        this.gridFsTemplate = gridFsTemplate;
        this.knowledgeNodeService = knowledgeNodeService;
    }

    public String findByNodeId(Long id) throws IOException {
        String resourceId = knowledgeNodeService.findById(id).getResourceId();
        if (log.isDebugEnabled()) {
            log.debug("resource id = {}", resourceId);
        }
        if (resourceId == null) {
            return null;
        }
        GridFSFile file = gridFsTemplate.findOne(new Query(Criteria.where("_id").is(resourceId)));
        if (file == null) {
            return null;
        }
        return new BufferedReader(new InputStreamReader(gridFsTemplate.getResource(file).getInputStream()))
            .lines().collect(Collectors.joining(System.lineSeparator()));
    }

    private void deleteNodeStorage(Long id) {
        String resourceId = knowledgeNodeService.findById(id).getResourceId();
        if (resourceId == null) {
            throw new IllegalArgumentException();
        }
        gridFsTemplate.delete(new Query(Criteria.where("_id").is(resourceId)));
    }

    public String saveNodeStorage(String resource, Long id) {
        KnowledgeNode node = knowledgeNodeService.findById(id);
        if (node.getResourceId() != null) {
            deleteNodeStorage(id);
        }
        String resourceId = gridFsTemplate.store(new ByteArrayInputStream(resource.getBytes()),
            RandomStringUtils.randomAlphanumeric(19))
            .toString();
        node.setResourceId(resourceId);
        knowledgeNodeService.save(node);
        return resourceId;
    }

    public GridFsResource findById(String id) {
        GridFSFile file = gridFsTemplate.findOne(new Query(Criteria.where("_id").is(id)));
        if (file == null) {
            throw new NullPointerException();
        }
        return gridFsTemplate.getResource(file);
    }

    public void delete(String id) {
        gridFsTemplate.delete(new Query(Criteria.where("_id").is(id)));
    }

    public String save(MultipartFile file) throws IOException {
        String digest = DigestUtils.md5DigestAsHex(file.getInputStream());
        GridFSFile fsFile = gridFsTemplate.findOne(new Query(Criteria.where("md5").is(digest)));
        if (fsFile == null) {
            return gridFsTemplate.store(
                file.getInputStream(),
                file.getOriginalFilename(),
                file.getContentType()
            ).toString();
        }
        return fsFile.getObjectId().toHexString();
    }

}
