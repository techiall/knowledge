package top.techial.knowledge.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import top.techial.beans.ResultBean;
import top.techial.knowledge.domain.NodeRelation;
import top.techial.knowledge.service.NodeRelationService;
import top.techial.knowledge.vo.RelationVO;

import java.util.List;

/**
 * @author techial
 */
@RestController
@RequestMapping("/api/relation")
public class KnowledgeRelationController {
    private final NodeRelationService nodeRelationService;

    public KnowledgeRelationController(NodeRelationService nodeRelationService) {
        this.nodeRelationService = nodeRelationService;
    }

    @GetMapping
    public ResultBean<Page<NodeRelation>> findAll(@PageableDefault Pageable pageable) {
        return new ResultBean<>(nodeRelationService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResultBean<NodeRelation> findAll(@PathVariable Long id) {
        return new ResultBean<>(nodeRelationService.findById(id));
    }

    @GetMapping("/name")
    public ResultBean<List<NodeRelation>> findByNameRelation(@RequestParam(value = "query") String name) {
        return new ResultBean<>(nodeRelationService.findFirstByStartNodeName(name));
    }

    @PostMapping
    public ResultBean<NodeRelation> save(@RequestBody RelationVO relationVO) {
        return new ResultBean<>(nodeRelationService.save(relationVO));
    }

    @PutMapping("/{id}")
    public ResultBean<NodeRelation> updateById(@PathVariable Long id, @RequestBody RelationVO relationVO) {
        return new ResultBean<>(nodeRelationService.updateById(id, relationVO));
    }

    @DeleteMapping("/{id}")
    public ResultBean<Boolean> deleteById(@PathVariable Long id) {
        nodeRelationService.deleteById(id);
        return new ResultBean<>(true);
    }
}
