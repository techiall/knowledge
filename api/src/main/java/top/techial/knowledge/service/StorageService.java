package top.techial.knowledge.service;

import com.mongodb.client.gridfs.model.GridFSFile;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
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

    public String findById(Long id) throws IOException {
        String resourceId = knowledgeNodeService.findById(id).getResourceId();
        if (log.isDebugEnabled()) {
            log.debug("resource id = {}", resourceId);
        }
        GridFSFile file = gridFsTemplate.findOne(new Query(Criteria.where("_id").is(resourceId)));
        if (file == null) {
            throw new NullPointerException();
        }
        return new BufferedReader(new InputStreamReader(gridFsTemplate.getResource(file).getInputStream()))
            .lines().collect(Collectors.joining(System.lineSeparator()));
    }

    public void delete(Long id) {
        String resourceId = knowledgeNodeService.findById(id).getResourceId();
        if (resourceId == null) {
            throw new IllegalArgumentException();
        }
        gridFsTemplate.delete(new Query(Criteria.where("_id").is(resourceId)));
    }

    public String save(String resource, Long id) {
        KnowledgeNode node = knowledgeNodeService.findById(id);
        if (node.getResourceId() != null) {
            throw new IllegalArgumentException();
        }
        String resourceId = gridFsTemplate.store(new ByteArrayInputStream(resource.getBytes()), RandomStringUtils.randomAlphanumeric(19))
            .toString();
        node.setResourceId(resourceId);
        knowledgeNodeService.save(node);
        return resourceId;
    }

    public String update(Long id, String resource) {
        KnowledgeNode node = knowledgeNodeService.findById(id);
        if (node.getResourceId() == null) {
            throw new IllegalArgumentException();
        }
        this.delete(id);
        String resourceId = gridFsTemplate.store(new ByteArrayInputStream(resource.getBytes()), String.valueOf(id)).toString();
        node.setResourceId(resourceId);
        knowledgeNodeService.save(node);
        return resourceId;
    }
}
