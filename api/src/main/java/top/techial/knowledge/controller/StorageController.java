package top.techial.knowledge.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.techial.beans.ResultBean;
import top.techial.knowledge.domain.Storage;
import top.techial.knowledge.service.FileStorageService;
import top.techial.knowledge.service.NodeService;
import top.techial.knowledge.service.StorageService;

import java.util.HashMap;
import java.util.Map;

/**
 * @author techial
 */
@RestController
@RequestMapping("/api/storage")
@Log4j2
public class StorageController {
    private final StorageService storageService;
    private final NodeService nodeService;
    private final FileStorageService fileStorageService;

    public StorageController(
            StorageService storageService,
            NodeService nodeService,
            FileStorageService fileStorageService
    ) {
        this.storageService = storageService;
        this.nodeService = nodeService;
        this.fileStorageService = fileStorageService;
    }

    /**
     * 文本上传，对应 node 节点
     */
    @PostMapping("/text/{id}")
    public ResultBean<Long> save(@RequestBody(required = false) String text, @PathVariable Long id) {
        nodeService.saveText(text, id);
        return new ResultBean<>(id);
    }

    /**
     * 文本获取，对应 node 节点
     */
    @GetMapping("/text/{id}")
    public ResultBean<String> findById(@PathVariable Long id) {
        return new ResultBean<>(nodeService.findText(id));
    }

    @PostMapping
    public Map<String, Object> save(@RequestParam MultipartFile file) {
        String sha1 = fileStorageService.upload(file);
        storageService.save(sha1, file);
        Map<String, Object> map = new HashMap<>();
        map.put("code", 0);
        map.put("msg", "Success");
        map.put("link", String.format("api/storage/preview/%s", sha1));
        return map;
    }

    @DeleteMapping("/{id}")
    public ResultBean<Boolean> delete(@PathVariable String id) {
        fileStorageService.delete(id);
        storageService.deleteById(id);
        return new ResultBean<>(true);
    }


    @GetMapping(value = "/preview/{id}")
    public ResponseEntity<Resource> preview(@PathVariable String id) {
        Storage storage = storageService.findById(id);
        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_TYPE, storageService.findById(id).getContentType())
                .body(fileStorageService.findByHash(storage.getId()));
    }

    @GetMapping(value = "/download/{id}")
    public ResponseEntity<Resource> download(@PathVariable String id) {
        Storage storage = storageService.findById(id);
        return ResponseEntity.ok().header(
                HttpHeaders.CONTENT_DISPOSITION,
                String.format("attachment; filename=\"%s\"", storage.getOriginalFilename())
        ).body(fileStorageService.findByHash(id));
    }

}
