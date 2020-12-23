package cc.techial.knowledge.web.rest;

import cc.techial.knowledge.beans.ResultBean;
import cc.techial.knowledge.domain.Node;
import cc.techial.knowledge.repository.StorageRepository;
import cc.techial.knowledge.repository.search.NodeSearchRepository;
import cc.techial.knowledge.service.FileStorageService;
import cc.techial.knowledge.service.StorageService;
import cc.techial.knowledge.web.rest.errors.NodeNotFoundException;
import org.springframework.core.io.Resource;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.TimeUnit;

/**
 * @author techial
 */
@RestController
@RequestMapping("/api/storage")
public class StorageController {

    private final StorageService storageService;
    private final FileStorageService fileStorageService;
    private final StorageRepository storageRepository;
    private final NodeSearchRepository nodeSearchRepository;

    public StorageController(StorageService storageService,
                             FileStorageService fileStorageService,
                             StorageRepository storageRepository,
                             NodeSearchRepository nodeSearchRepository) {
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
    public ResultBean save(@RequestBody(required = false) String text,
                           @PathVariable long id) {
        final var node = nodeSearchRepository.findById(id)
                                             .map(it -> {
                                                 it.setText(text);
                                                 return nodeSearchRepository.index(it);
                                             })
                                             .orElseThrow(NodeNotFoundException::new);
        return ResultBean.ok(node.getId());
    }

    /**
     * 文本获取，对应 node 节点
     */
    @GetMapping("/text/{id}")
    public ResultBean findById(@PathVariable long id) {
        final var s = nodeSearchRepository.findById(id)
                                          .map(Node::getText)
                                          .orElse(null);
        return ResultBean.ok(s);
    }

    @PostMapping
    public ResultBean save(@RequestParam MultipartFile file) {
        final var sha1 = fileStorageService.upload(file);
        storageService.save(sha1, file);
        return ResultBean.ok(String.format("/api/storage/preview/%s", sha1));
    }

    @DeleteMapping("/{id}")
    public ResultBean delete(@PathVariable String id) {
        fileStorageService.delete(id);
        storageRepository.deleteById(id);
        return ResultBean.ok(true);
    }

    @GetMapping(value = "/preview/{id}")
    public ResponseEntity<Resource> preview(@PathVariable String id) {
        final var storage = storageService.findById(id);
        return ResponseEntity.ok()
                             .header(HttpHeaders.CONTENT_TYPE, storageService.findById(id).getContentType())
                             .header(HttpHeaders.CACHE_CONTROL, CacheControl.maxAge(1, TimeUnit.HOURS).cachePublic().getHeaderValue())
                             .body(fileStorageService.findByHash(storage.getId()));
    }

    @GetMapping(value = "/download/{id}")
    public ResponseEntity<Resource> download(@PathVariable String id) {
        var storage = storageService.findById(id);
        return ResponseEntity.ok()
                             .header(HttpHeaders.CONTENT_DISPOSITION, String.format("attachment; filename=\"%s\"", storage.getOriginalFilename()))
                             .body(fileStorageService.findByHash(id));
    }
}
