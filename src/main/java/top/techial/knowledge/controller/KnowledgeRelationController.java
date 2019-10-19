package top.techial.knowledge.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.techial.knowledge.beans.ResultBean;
import top.techial.knowledge.domain.KnowledgeNodeRelation;
import top.techial.knowledge.service.KnowledgeNodeRelationService;

import java.util.List;

/**
 * @author techial
 */
@RestController
@RequestMapping("/api/knowledge/relation")
public class KnowledgeRelationController {
    private final KnowledgeNodeRelationService knowledgeNodeRelationService;

    public KnowledgeRelationController(KnowledgeNodeRelationService knowledgeNodeRelationService) {
        this.knowledgeNodeRelationService = knowledgeNodeRelationService;
    }

    @GetMapping("/name/{name}")
    public ResultBean<List<KnowledgeNodeRelation>> findByNameRelation(@PathVariable String name) {
        return new ResultBean<>(knowledgeNodeRelationService.findFirstByStartNodeName(name));
    }
}
