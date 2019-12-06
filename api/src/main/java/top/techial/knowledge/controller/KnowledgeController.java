package top.techial.knowledge.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import top.techial.beans.ResultBean;
import top.techial.knowledge.dto.NodeBaseDTO;
import top.techial.knowledge.dto.NodeDTO;
import top.techial.knowledge.dto.NodeInfoDTO;
import top.techial.knowledge.service.KnowledgeNodeService;
import top.techial.knowledge.vo.NodeVO;

import java.util.Set;

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
    public ResultBean<Page<NodeDTO>> findAll(
        @PageableDefault Pageable pageable,
        @RequestParam(defaultValue = "10", required = false) int depth) {
        return new ResultBean<>(knowledgeNodeService.findAll(pageable, depth));
    }

    @GetMapping("/{id}")
    public ResultBean<NodeInfoDTO> findById(@PathVariable Long id) {
        return new ResultBean<>(new NodeInfoDTO(knowledgeNodeService.findById(id)));
    }

    @PostMapping
    public ResultBean<NodeInfoDTO> save(@RequestBody NodeVO nodeVO) {
        return new ResultBean<>(new NodeInfoDTO(knowledgeNodeService.save(nodeVO)));
    }

    @PutMapping("/{id}")
    public ResultBean<NodeInfoDTO> update(@PathVariable Long id, @RequestBody NodeVO nodeVO) {
        return new ResultBean<>(new NodeInfoDTO(knowledgeNodeService.update(id, nodeVO)));
    }

    @DeleteMapping("/{id}")
    public ResultBean<Boolean> deleteById(@PathVariable Long id) {
        knowledgeNodeService.deleteById(id);
        return new ResultBean<>(true);
    }

    @DeleteMapping
    public ResultBean<Boolean> deleteByIds(@RequestParam Set<Long> ids) {
        knowledgeNodeService.deleteByIds(ids);
        return new ResultBean<>(true);
    }

    @GetMapping("/{id}/graph")
    public ResultBean<Object> findByIdGraph(@PathVariable Long id) {
        return new ResultBean<>(knowledgeNodeService.findByIdGraph(id));
    }

    @GetMapping("/{id}/child")
    public ResultBean<Set<NodeDTO>> findChildNode(
        @PathVariable Long id,
        @RequestParam(required = false, defaultValue = "10") int depth) {
        return new ResultBean<>(knowledgeNodeService.findByChildNode(id, depth));
    }

    @GetMapping("/name")
    public ResultBean<Page<NodeBaseDTO>> findByName(@RequestParam(name = "query") String name, @PageableDefault Pageable pageable) {
        return new ResultBean<>(knowledgeNodeService.findByNameLike("*" + name + "*", pageable).map(NodeBaseDTO::new));
    }

}
