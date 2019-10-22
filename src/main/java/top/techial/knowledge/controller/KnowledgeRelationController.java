package top.techial.knowledge.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import top.techial.knowledge.beans.ResultBean;
import top.techial.knowledge.domain.KnowledgeNodeRelation;
import top.techial.knowledge.service.KnowledgeNodeRelationService;
import top.techial.knowledge.vo.RelationVO;

import java.util.List;

/**
 * @author techial
 */
@RestController
@RequestMapping("/api/relation")
public class KnowledgeRelationController {
    private final KnowledgeNodeRelationService knowledgeNodeRelationService;

    public KnowledgeRelationController(KnowledgeNodeRelationService knowledgeNodeRelationService) {
        this.knowledgeNodeRelationService = knowledgeNodeRelationService;
    }

    @GetMapping
    public ResultBean<Page<KnowledgeNodeRelation>> findAll(@PageableDefault Pageable pageable) {
        return new ResultBean<>(knowledgeNodeRelationService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResultBean<KnowledgeNodeRelation> findAll(@PathVariable Long id) {
        return new ResultBean<>(knowledgeNodeRelationService.findById(id));
    }

    @GetMapping("/name")
    public ResultBean<List<KnowledgeNodeRelation>> findByNameRelation(@RequestParam(value = "query") String name) {
        return new ResultBean<>(knowledgeNodeRelationService.findFirstByStartNodeName(name));
    }

    @PostMapping
    public ResultBean<KnowledgeNodeRelation> save(@RequestBody RelationVO relationVO) {
        return new ResultBean<>(knowledgeNodeRelationService.save(relationVO));
    }

    @PutMapping("/{id}")
    public ResultBean<KnowledgeNodeRelation> updateById(@PathVariable Long id, @RequestBody RelationVO relationVO) {
        return new ResultBean<>(knowledgeNodeRelationService.updateById(id, relationVO));
    }

    @DeleteMapping("/{id}")
    public ResultBean<Boolean> deleteById(@PathVariable Long id) {
        knowledgeNodeRelationService.deleteById(id);
        return new ResultBean<>(true);
    }
}
