package top.techial.knowledge.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import top.techial.beans.ResultBean;
import top.techial.knowledge.domain.NodeRelation;
import top.techial.knowledge.dto.NodeInfoDTO;
import top.techial.knowledge.dto.NodeRelationDTO;
import top.techial.knowledge.service.NodeRelationService;
import top.techial.knowledge.vo.ParentVO;
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
    public ResultBean<NodeRelationDTO> findAll(@PathVariable Long id) {
        return new ResultBean<>(new NodeRelationDTO(nodeRelationService.findById(id)));
    }

    @GetMapping("/start/name")
    public ResultBean<List<NodeRelationDTO>> findByNameRelation(@RequestParam(value = "query") String name) {
        return new ResultBean<>(nodeRelationService.findByStartNodeName(name));
    }

    @PostMapping
    public ResultBean<NodeRelationDTO> save(@RequestBody RelationVO relationVO) {
        return new ResultBean<>(new NodeRelationDTO(nodeRelationService.save(relationVO)));
    }

    @PutMapping("/{id}")
    public ResultBean<NodeRelationDTO> updateById(@PathVariable Long id, @RequestBody RelationVO relationVO) {
        return new ResultBean<>(new NodeRelationDTO(nodeRelationService.updateById(id, relationVO)));
    }

    @PatchMapping("/parent")
    public ResultBean<NodeInfoDTO> parent(@RequestBody ParentVO parentVO) {
        return new ResultBean<>(new NodeInfoDTO(nodeRelationService.updateParent(parentVO)));
    }

    @DeleteMapping("/{id}")
    public ResultBean<Boolean> deleteById(@PathVariable Long id) {
        nodeRelationService.deleteById(id);
        return new ResultBean<>(true);
    }
}
