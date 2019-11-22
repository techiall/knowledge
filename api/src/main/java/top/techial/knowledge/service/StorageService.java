package top.techial.knowledge.service;

import com.mongodb.client.gridfs.model.GridFSFile;
import lombok.extern.log4j.Log4j2;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

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

    public GridFsResource findById(String id) {
        GridFSFile file = gridFsTemplate.findOne(new Query(Criteria.where("_id").is(id)));
        if (file == null) {
            throw new NullPointerException();
        }
        return gridFsTemplate.getResource(file);
    }

    public ObjectId findByIdResource(Long id) {
        GridFSFile file = gridFsTemplate.findOne(new Query(Criteria.where("_id").is(id)));
        return file == null ? null : file.getObjectId();
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

    public ObjectId update(Long id) {
        String resourceId = knowledgeNodeService.findById(id).getResourceId();
        if (resourceId == null) {
            throw new IllegalArgumentException();
        }
        return null;
//        return gridFsTemplate.store(new ByteArrays(resource.getBytes())).toString();
    }
}
