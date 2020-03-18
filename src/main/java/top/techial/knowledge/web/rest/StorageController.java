package top.techial.knowledge.web.rest;

import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.techial.knowledge.beans.ResultBean;
import top.techial.knowledge.domain.Node;
import top.techial.knowledge.domain.Storage;
import top.techial.knowledge.repository.StorageRepository;
import top.techial.knowledge.repository.search.NodeSearchRepository;
import top.techial.knowledge.service.FileStorageService;
import top.techial.knowledge.service.StorageService;
import top.techial.knowledge.web.rest.errors.NodeNotFoundException;

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
    private final FileStorageService fileStorageService;
    private final StorageRepository storageRepository;
    private final NodeSearchRepository nodeSearchRepository;

    public StorageController(
            StorageService storageService,
            FileStorageService fileStorageService,
            StorageRepository storageRepository,
            NodeSearchRepository nodeSearchRepository
    ) {
        this.storageService = storageService;
        this.fileStorageService = fileStorageService;
        this.storageRepository = storageRepository;
        this.nodeSearchRepository = nodeSearchRepository;
    }

    /**
     * 文本上传，对应 node 节点
     */
    @PostMapping("/text/{id}")
    @PreAuthorize("hasAnyAuthority(#id)")
    public ResultBean<Long> save(@RequestBody(required = false) String text, @PathVariable Long id) {
        Node node = nodeSearchRepository.findById(id).orElseThrow(NodeNotFoundException::new);
        nodeSearchRepository.index(node.setText(text));
        return ResultBean.ok(id);
    }

    /**
     * 文本获取，对应 node 节点
     */
    @GetMapping("/text/{id}")
    public ResultBean<String> findById(@PathVariable Long id) {
        Node node = nodeSearchRepository.findById(id).orElse(new Node());
        return ResultBean.ok(node.getText());
    }

    @PostMapping
    public Map<String, Object> save(@RequestParam MultipartFile file) {
        String sha1 = fileStorageService.upload(file);
        storageService.save(sha1, file);
        Map<String, Object> map = new HashMap<>();
        map.put("code", 200);
        map.put("msg", "OK");
        map.put("link", String.format("https://knowledge.pchelper666.com/api/storage/preview/%s", sha1));
        return map;
    }

    @DeleteMapping("/{id}")
    public ResultBean<Boolean> delete(@PathVariable String id) {
        fileStorageService.delete(id);
        storageRepository.deleteById(id);
        return ResultBean.ok(true);
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
