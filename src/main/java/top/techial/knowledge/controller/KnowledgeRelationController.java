package top.techial.knowledge.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import top.techial.knowledge.beans.ResultBean;
import top.techial.knowledge.domain.KnowledgeNodeRelation;
import top.techial.knowledge.service.KnowledgeNodeRelationService;

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

}
