package top.techial.knowledge.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;
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

    @PostMapping("/{id}")
    public ResultBean<String> save(@RequestBody String resource, @PathVariable Long id) {
        return new ResultBean<>(storageService.save(resource, id));
    }

    @GetMapping("/{id}")
    public ResultBean<String> findById(@PathVariable Long id) throws IOException {
        return new ResultBean<>(storageService.findById(id));
    }

    @PutMapping("/{id}")
    public ResultBean<String> update(@PathVariable Long id, @RequestBody String body) {
        return new ResultBean<>(storageService.update(id, body));
    }

    @DeleteMapping("/{id}")
    public ResultBean<Boolean> delete(@PathVariable Long id) {
        storageService.delete(id);
        return new ResultBean<>(true);
    }

}
