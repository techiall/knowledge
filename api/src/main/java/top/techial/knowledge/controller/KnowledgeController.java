package top.techial.knowledge.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import top.techial.beans.ResultBean;
import top.techial.knowledge.domain.KnowledgeNode;
import top.techial.knowledge.domain.OperatorMessageEnum;
import top.techial.knowledge.dto.NodeBaseDTO;
import top.techial.knowledge.dto.NodeDTO;
import top.techial.knowledge.dto.NodeInfoDTO;
import top.techial.knowledge.exception.UserNotFoundNodeException;
import top.techial.knowledge.mapper.KnowledgeNodeMapper;
import top.techial.knowledge.security.UserPrincipal;
import top.techial.knowledge.service.KnowledgeNodeService;
import top.techial.knowledge.service.RecordService;
import top.techial.knowledge.vo.NodeVO;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * @author techial
 */
@RestController
@RequestMapping("/api/node")
public class KnowledgeController {
    private final KnowledgeNodeService knowledgeNodeService;
    private final RecordService recordService;

    public KnowledgeController(KnowledgeNodeService knowledgeNodeService, RecordService recordService) {
        this.knowledgeNodeService = knowledgeNodeService;
        this.recordService = recordService;
    }

    @GetMapping
    public ResultBean<Page<NodeDTO>> findAll(
        @AuthenticationPrincipal UserPrincipal userPrincipal,
        @PageableDefault(sort = "createTime", direction = Sort.Direction.ASC) Pageable pageable
    ) {
        return new ResultBean<>(knowledgeNodeService.findAll(userPrincipal.getId(), pageable));
    }

    @GetMapping("/{id}")
    public ResultBean<Object> findById(
        @AuthenticationPrincipal UserPrincipal userPrincipal,
        @PathVariable Long id
    ) {
        KnowledgeNode node = knowledgeNodeService.findById(id);
        if (!Objects.equals(node.getUserId(), userPrincipal.getId())) {
            throw new UserNotFoundNodeException(userPrincipal.getId(), id);
        }
        return new ResultBean<>(KnowledgeNodeMapper.INSTANCE.toNodeInfoDTO(knowledgeNodeService.findById(id)));
    }

    @PostMapping
    public ResultBean<NodeInfoDTO> save(
        @AuthenticationPrincipal UserPrincipal userPrincipal,
        @RequestBody NodeVO nodeVO
    ) {
        KnowledgeNode node = knowledgeNodeService.save(userPrincipal.getId(), nodeVO);

        recordService.save(node.getId(), userPrincipal.getId(),
            OperatorMessageEnum.ADD_NODE, nodeVO);

        return new ResultBean<>(KnowledgeNodeMapper.INSTANCE.toNodeInfoDTO(node));
    }

    @PutMapping("/{id}")
    public ResultBean<NodeInfoDTO> update(
        @AuthenticationPrincipal UserPrincipal userPrincipal,
        @PathVariable Long id,
        @RequestBody NodeVO nodeVO
    ) {
        KnowledgeNode node = knowledgeNodeService.update(id, userPrincipal.getId(), nodeVO);

        recordService.save(node.getId(), userPrincipal.getId(),
            OperatorMessageEnum.UPDATE_NODE_PROPER, nodeVO);

        return new ResultBean<>(KnowledgeNodeMapper.INSTANCE.toNodeInfoDTO(node));
    }

    @PatchMapping("/{id}/name")
    public ResultBean<NodeInfoDTO> updateName(
        @AuthenticationPrincipal UserPrincipal userPrincipal,
        @PathVariable Long id,
        @RequestBody NodeVO nodeVO
    ) {
        KnowledgeNode node = knowledgeNodeService
            .updateName(id, userPrincipal.getId(), nodeVO.getName());

        recordService.save(node.getId(), userPrincipal.getId(),
            OperatorMessageEnum.UPDATE_NODE_NAME, nodeVO);

        return new ResultBean<>(KnowledgeNodeMapper.INSTANCE.toNodeInfoDTO(node));
    }


    @DeleteMapping("/{id}")
    public ResultBean<Boolean> deleteById(
        @AuthenticationPrincipal UserPrincipal userPrincipal,
        @PathVariable Long id
    ) {
        knowledgeNodeService.deleteById(id, userPrincipal.getId());
        recordService.deleteByNodeId(id);
        return new ResultBean<>(true);
    }

    @DeleteMapping
    public ResultBean<Boolean> deleteByIds(
        @RequestBody Set<Long> ids,
        @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        knowledgeNodeService.deleteByIds(ids, userPrincipal.getId());
        recordService.deleteByNodeIds(ids);
        return new ResultBean<>(true);
    }

    @GetMapping("/{id}/graph")
    public ResultBean<Object> findByIdGraph(@PathVariable Long id) {
        return new ResultBean<>(knowledgeNodeService.findByIdGraph(id));
    }

    @GetMapping("/{id}/link")
    public ResultBean<Map<String, List<NodeBaseDTO>>> getChildAndParent(
        @PathVariable Long id,
        @RequestParam(required = false, defaultValue = "10") int depth
    ) {
        return new ResultBean<>(knowledgeNodeService.getChildAndParent(id, depth));
    }

    @GetMapping("/{id}/child")
    public ResultBean<List<NodeDTO>> findChildNode(
        @PathVariable Long id,
        @AuthenticationPrincipal UserPrincipal userPrincipal,
        @RequestParam(required = false, defaultValue = "10") int depth
    ) {
        return new ResultBean<>(knowledgeNodeService.findByChildNode(id, userPrincipal.getId(), depth));
    }

    @GetMapping("/name")
    public ResultBean<Page<NodeBaseDTO>> findByName(
        @RequestParam(name = "query") String name,
        @AuthenticationPrincipal UserPrincipal userPrincipal,
        @PageableDefault Pageable pageable
    ) {
        return new ResultBean<>(knowledgeNodeService
            .findByNameLike("*" + name + "*", userPrincipal.getId(), pageable)
            .map(KnowledgeNodeMapper.INSTANCE::toNodeBaseDTO));
    }

}
