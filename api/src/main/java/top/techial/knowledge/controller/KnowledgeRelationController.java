package top.techial.knowledge.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import top.techial.beans.ResultBean;
import top.techial.knowledge.domain.NodeRelation;
import top.techial.knowledge.dto.NodeInfoDTO;
import top.techial.knowledge.dto.RelationDTO;
import top.techial.knowledge.service.NodeRelationService;
import top.techial.knowledge.service.RecordService;
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
    private final RecordService recordService;

    public KnowledgeRelationController(NodeRelationService nodeRelationService, RecordService recordService) {
        this.nodeRelationService = nodeRelationService;
        this.recordService = recordService;
    }

    @GetMapping
    public ResultBean<Page<NodeRelation>> findAll(@PageableDefault Pageable pageable) {
        return new ResultBean<>(nodeRelationService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResultBean<RelationDTO> findAll(@PathVariable Long id) {
        return new ResultBean<>(new RelationDTO(nodeRelationService.findById(id)));
    }

    @GetMapping("/start/name")
    public ResultBean<List<RelationDTO>> findByNameRelation(@RequestParam(value = "query") String name) {
        return new ResultBean<>(nodeRelationService.findByStartNodeName(name));
    }

    @PostMapping
    public ResultBean<RelationDTO> save(@RequestBody RelationVO relationVO) {
        return new ResultBean<>(new RelationDTO(nodeRelationService.save(relationVO)));
    }

    @PutMapping("/{id}")
    public ResultBean<RelationDTO> updateById(@PathVariable Long id, @RequestBody RelationVO relationVO) {
        return new ResultBean<>(new RelationDTO(nodeRelationService.updateById(id, relationVO)));
    }

    @PatchMapping("/parent")
    public ResultBean<NodeInfoDTO> parent(@RequestBody ParentVO parentVO) {
        return new ResultBean<>(new NodeInfoDTO(nodeRelationService.updateParent(parentVO)));
    }

    @DeleteMapping("/{id}")
    public ResultBean<Boolean> deleteById(@PathVariable Long id) {
        nodeRelationService.deleteById(id);
        recordService.deleteByNodeId(id);
        return new ResultBean<>(true);
    }
}
