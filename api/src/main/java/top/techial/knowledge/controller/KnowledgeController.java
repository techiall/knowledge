package top.techial.knowledge.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import top.techial.beans.ResultBean;
import top.techial.knowledge.domain.KnowledgeNode;
import top.techial.knowledge.dto.NodeDTO;
import top.techial.knowledge.service.KnowledgeNodeService;
import top.techial.knowledge.vo.NodeVO;

/**
 * @author techial
 */
@RestController
@RequestMapping("/api/node")
public class KnowledgeController {
    private final KnowledgeNodeService knowledgeNodeService;

    public KnowledgeController(KnowledgeNodeService knowledgeNodeService) {
        this.knowledgeNodeService = knowledgeNodeService;
    }

    @GetMapping
    public ResultBean<Page<NodeDTO>> findAll(@PageableDefault Pageable pageable) {
        return new ResultBean<>(knowledgeNodeService.findAllByIsParentNode(pageable));
    }

    @GetMapping("/{id}")
    public ResultBean<KnowledgeNode> findById(@PathVariable Long id) {
        return new ResultBean<>(knowledgeNodeService.findById(id));
    }

    @GetMapping("/{id}/graph")
    public ResultBean<Object> findByIdGraph(@PathVariable Long id) {
        return new ResultBean<>(knowledgeNodeService.findByIdGraph(id));
    }

    @GetMapping("/name")
    public ResultBean<KnowledgeNode> findByName(@RequestParam(name = "query") String name) {
        return new ResultBean<>(knowledgeNodeService.findByName(name));
    }

    @PostMapping
    public ResultBean<KnowledgeNode> save(@RequestBody NodeVO nodeVO) {
        return new ResultBean<>(knowledgeNodeService.save(nodeVO));
    }

    @PutMapping("/{id}")
    public ResultBean<KnowledgeNode> update(@PathVariable Long id, @RequestBody NodeVO nodeVO) {
        return new ResultBean<>(knowledgeNodeService.save(nodeVO.toKnowledgeNode().setId(id)));
    }

    @DeleteMapping("/{id}")
    public ResultBean<Boolean> deleteById(@PathVariable Long id) {
        knowledgeNodeService.deleteById(id);
        return new ResultBean<>(true);
    }

}
