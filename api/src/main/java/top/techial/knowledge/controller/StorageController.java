package top.techial.knowledge.controller;

import lombok.extern.log4j.Log4j2;
import org.bson.types.ObjectId;
import org.springframework.core.io.Resource;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.techial.beans.ResultBean;
import top.techial.knowledge.service.StorageService;

import java.io.IOException;

/**
 * @author techial
 */
@RestController
@RequestMapping("/api/storage")
@Log4j2
public class StorageController {
    private final StorageService storageService;

    public StorageController(StorageService storageService) {
        this.storageService = storageService;
    }

    @PostMapping
    public ResultBean<String> save(@RequestParam MultipartFile file) throws IOException {
        return new ResultBean<>(storageService.save(file));
    }

    @GetMapping("/{id}")
    public ResultBean<ObjectId> findById(@PathVariable Long id) {
        return new ResultBean<>(storageService.findByIdResource(id));
    }

    @PutMapping("/{id}")
    public ResultBean<ObjectId> update(@PathVariable Long id) {
        return new ResultBean<>(storageService.update(id));
    }

    @DeleteMapping("/{id}")
    public ResultBean<Boolean> delete(@PathVariable String id) {
        storageService.delete(id);
        return new ResultBean<>(true);
    }

    @GetMapping(value = "/preview/{id}")
    public ResponseEntity<Resource> preview(@PathVariable String id) {
        GridFsResource gridFsResource = storageService.findById(id);
        return ResponseEntity
            .ok()
            .header(HttpHeaders.CONTENT_TYPE, gridFsResource.getContentType())
            .body(gridFsResource);
    }
}
