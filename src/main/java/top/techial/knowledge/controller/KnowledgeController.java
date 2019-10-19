package top.techial.knowledge.controller;

import org.springframework.web.bind.annotation.*;
import top.techial.knowledge.beans.ResultBean;
import top.techial.knowledge.domain.KnowledgeNode;
import top.techial.knowledge.service.KnowledgeNodeService;
import top.techial.knowledge.vo.NodeVO;

/**
 * @author techial
 */
@RestController
@RequestMapping("/api/knowledge")
public class KnowledgeController {
    private final KnowledgeNodeService knowledgeNodeService;

    public KnowledgeController(KnowledgeNodeService knowledgeNodeService) {
        this.knowledgeNodeService = knowledgeNodeService;
    }

    @PostMapping
    public ResultBean<KnowledgeNode> save(@RequestBody NodeVO nodeVO) {
        return new ResultBean<>(knowledgeNodeService.save(nodeVO.toKnowledgeNode()));
    }

    @PutMapping("/{id}")
    public ResultBean<KnowledgeNode> update(@PathVariable Long id, @RequestBody NodeVO nodeVO) {
        return new ResultBean<>(knowledgeNodeService.save(nodeVO.toKnowledgeNode().setId(id)));
    }

    @GetMapping("/{id}")
    public ResultBean<KnowledgeNode> findById(@PathVariable Long id) {
        return new ResultBean<>(knowledgeNodeService.findById(id));
    }

    @DeleteMapping("/{id}")
    public ResultBean<Boolean> deleteById(@PathVariable Integer id) {
        knowledgeNodeService.deleteById(id);
        return new ResultBean<>(true);
    }

}
