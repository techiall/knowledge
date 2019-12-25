package top.techial.knowledge.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import top.techial.beans.ResultBean;
import top.techial.knowledge.domain.NodeRelation;
import top.techial.knowledge.domain.OperatorMessageEnum;
import top.techial.knowledge.dto.NodeInfoDTO;
import top.techial.knowledge.dto.RelationDTO;
import top.techial.knowledge.mapper.KnowledgeNodeMapper;
import top.techial.knowledge.mapper.NodeRelationMapper;
import top.techial.knowledge.security.UserPrincipal;
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
    private final ObjectMapper objectMapper;

    public KnowledgeRelationController(NodeRelationService nodeRelationService, RecordService recordService, ObjectMapper objectMapper) {
        this.nodeRelationService = nodeRelationService;
        this.recordService = recordService;
        this.objectMapper = objectMapper;
    }

    @GetMapping
    public ResultBean<Page<NodeRelation>> findAll(@PageableDefault Pageable pageable) {
        return new ResultBean<>(nodeRelationService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResultBean<RelationDTO> findAll(@PathVariable Long id) {
        return new ResultBean<>(NodeRelationMapper.INSTANCE.toRelationDTO(nodeRelationService.findById(id)));
    }

    @GetMapping("/start/name")
    public ResultBean<List<RelationDTO>> findByNameRelation(@RequestParam(value = "query") String name) {
        return new ResultBean<>(nodeRelationService.findByStartNodeName(name));
    }

    @PostMapping
    public ResultBean<RelationDTO> save(@RequestBody RelationVO relationVO, @AuthenticationPrincipal UserPrincipal userPrincipal) throws JsonProcessingException {
        NodeRelation relation = nodeRelationService.save(relationVO);

        recordService.save(relation.getStartNode().getId(), userPrincipal.getId(),
            OperatorMessageEnum.ADD_NODE_PROPERTY,
            String.format(OperatorMessageEnum.ADD_NODE_PROPERTY.getMessage(), objectMapper.writeValueAsString(relationVO)));

        return new ResultBean<>(NodeRelationMapper.INSTANCE.toRelationDTO(relation));
    }

    @PutMapping("/{id}")
    public ResultBean<RelationDTO> updateById(@PathVariable Long id, @RequestBody RelationVO relationVO, @AuthenticationPrincipal UserPrincipal userPrincipal) throws JsonProcessingException {
        NodeRelation relation = nodeRelationService.updateById(id, relationVO);

        recordService.save(relation.getStartNode().getId(), userPrincipal.getId(),
            OperatorMessageEnum.UPDATE_NODE_PROPERTY,
            String.format(OperatorMessageEnum.UPDATE_NODE_PROPERTY.getMessage(), objectMapper.writeValueAsString(relationVO)));

        return new ResultBean<>(NodeRelationMapper.INSTANCE.toRelationDTO(nodeRelationService.updateById(id, relationVO)));
    }

    @PatchMapping("/parent")
    public ResultBean<NodeInfoDTO> parent(@RequestBody ParentVO parentVO) {
        return new ResultBean<>(KnowledgeNodeMapper.INSTANCE.toNodeInfoDTO(nodeRelationService.updateParent(parentVO)));
    }

    @DeleteMapping("/{id}")
    public ResultBean<Boolean> deleteById(@PathVariable Long id, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        NodeRelation relation = nodeRelationService.findById(id);

        recordService.save(relation.getStartNode().getId(), userPrincipal.getId(),
            OperatorMessageEnum.DELETE_NODE_RELATION,
            String.format(OperatorMessageEnum.DELETE_NODE_RELATION.getMessage(), id));

        nodeRelationService.deleteById(id);
        return new ResultBean<>(true);
    }
}
