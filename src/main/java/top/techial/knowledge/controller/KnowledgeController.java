package top.techial.knowledge.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import top.techial.knowledge.beans.ResultBean;
import top.techial.knowledge.domain.KnowledgeNode;
import top.techial.knowledge.service.KnowledgeNodeRelationService;
import top.techial.knowledge.service.KnowledgeNodeService;
import top.techial.knowledge.vo.NodeVO;

/**
 * @author techial
 */
@RestController
@RequestMapping("/api/knowledge")
public class KnowledgeController {
    private final KnowledgeNodeService knowledgeNodeService;
    private final KnowledgeNodeRelationService knowledgeNodeRelationService;

    public KnowledgeController(KnowledgeNodeService knowledgeNodeService, KnowledgeNodeRelationService knowledgeNodeRelationService) {
        this.knowledgeNodeService = knowledgeNodeService;
        this.knowledgeNodeRelationService = knowledgeNodeRelationService;
    }

    @PostMapping
    public ResultBean<KnowledgeNode> save(@RequestBody NodeVO nodeVO) {
        return new ResultBean<>(knowledgeNodeService.save(nodeVO.toKnowledgeNode()));
    }

    @PutMapping("/{id}")
    public ResultBean<KnowledgeNode> update(@PathVariable Long id, @RequestBody NodeVO nodeVO) {
        return new ResultBean<>(knowledgeNodeService.save(nodeVO.toKnowledgeNode().setId(id)));
    }

    @GetMapping("/query")
    public ResultBean<Page<KnowledgeNode>> findByName(@RequestParam String name, Pageable pageable) {
        return new ResultBean<>(knowledgeNodeService.findByNameLike(name, pageable));
    }

    @GetMapping("/name/{name}")
    public ResultBean<KnowledgeNode> findByName(@PathVariable String name) {
        return new ResultBean<>(knowledgeNodeService.findByName(name));
    }


    @GetMapping("/{id}")
    public ResultBean<KnowledgeNode> findById(@PathVariable Long id) {
        return new ResultBean<>(knowledgeNodeService.findById(id));
    }

    @GetMapping("/{id}/graph")
    public ResultBean<Object> findByIdGraph(@PathVariable Long id) {
        return new ResultBean<>(knowledgeNodeService.findByIdGraph(id));
    }

    @DeleteMapping("/{id}")
    public ResultBean<Boolean> deleteById(@PathVariable Long id) {
        knowledgeNodeService.deleteById(id);
        return new ResultBean<>(true);
    }

}
