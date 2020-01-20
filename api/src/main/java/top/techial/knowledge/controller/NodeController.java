package top.techial.knowledge.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import top.techial.beans.ResultBean;
import top.techial.knowledge.domain.Labels;
import top.techial.knowledge.domain.Node;
import top.techial.knowledge.domain.OperatorMessageEnum;
import top.techial.knowledge.domain.Property;
import top.techial.knowledge.dto.NodeBaseDTO;
import top.techial.knowledge.dto.NodeInfoDTO;
import top.techial.knowledge.mapper.NodeMapper;
import top.techial.knowledge.security.UserPrincipal;
import top.techial.knowledge.service.NodeService;
import top.techial.knowledge.service.RecordService;
import top.techial.knowledge.vo.NodeVO;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author techial
 */
@RestController
@RequestMapping("/api/node")
public class NodeController {
    private final NodeService nodeService;
    private final RecordService recordService;

    public NodeController(NodeService nodeService, RecordService recordService) {
        this.nodeService = nodeService;
        this.recordService = recordService;
    }

    @GetMapping("/{id}")
    public ResultBean<NodeInfoDTO> findById(@PathVariable Long id) {
        Node node = nodeService.findById(id);
        return new ResultBean<>(NodeMapper.INSTANCE.toNodeInfoDTO(node));
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority(#nodeVO.itemId.toString())")
    public ResultBean<NodeInfoDTO> save(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @Valid @RequestBody NodeVO nodeVO
    ) {
        Node node = nodeService.save(nodeVO);

        recordService.save(node.getId(), userPrincipal.getId(),
                OperatorMessageEnum.ADD_NODE, nodeVO);

        return new ResultBean<>(NodeMapper.INSTANCE.toNodeInfoDTO(node));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority(#nodeVO.itemId.toString())")
    public ResultBean<NodeInfoDTO> update(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long id,
            @RequestBody NodeVO nodeVO
    ) {
        Node node = nodeService.findById(id);

        if (nodeVO != null && nodeVO.getName() != null) {
            node.setName(nodeVO.getName());
            recordService.save(node.getId(), userPrincipal.getId(),
                    OperatorMessageEnum.UPDATE_NODE_NAME, nodeVO);
        }
        if (nodeVO != null && nodeVO.getLabels() != null) {
            node.setLabels(new Labels().setLabels(nodeVO.getLabels()));
            recordService.save(node.getId(), userPrincipal.getId(),
                    OperatorMessageEnum.UPDATE_NODE_PROPER, nodeVO);
        }
        if (nodeVO != null && nodeVO.getProperty() != null) {
            node.setProperty(new Property().setProperty(nodeVO.getProperty()));
            recordService.save(node.getId(), userPrincipal.getId(),
                    OperatorMessageEnum.UPDATE_NODE_PROPERTY, nodeVO);
        }

        node = nodeService.save(node);

        return new ResultBean<>(NodeMapper.INSTANCE.toNodeInfoDTO(node));
    }

    @DeleteMapping("/{id}")
    public ResultBean<Boolean> deleteById(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long id
    ) {
        nodeService.deleteById(id);
        recordService.deleteByNodeId(id);
        return new ResultBean<>(true);
    }

    @DeleteMapping
    public ResultBean<Boolean> deleteByIds(
            @RequestBody Set<Long> ids,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        nodeService.deleteByIds(ids);
        recordService.deleteByNodeIds(ids);
        return new ResultBean<>(true);
    }

    @GetMapping("/{id}/graph")
    public ResultBean<Object> findByIdGraph(@PathVariable Long id) {
//        return new ResultBean<>(knowledgeNodeService.findByIdGraph(id));
        return null;
    }

    @GetMapping("/{id}/link")
    public ResultBean<Map<String, List<NodeBaseDTO>>> getChildAndParent(
            @PathVariable Long id,
            @RequestParam(required = false, defaultValue = "10") int depth
    ) {
        return new ResultBean<>(nodeService.getChildAndParent(id, depth));
    }

    @GetMapping("/{id}/child")
    public ResultBean<List<NodeBaseDTO>> findChildNode(
            @PathVariable Long id,
            @RequestParam(required = false, defaultValue = "10") int depth
    ) {
        return new ResultBean<>(nodeService.findByChildNode(id, depth));
    }

    @GetMapping("/name")
    public ResultBean<List<NodeBaseDTO>> findByName(
            @RequestParam(name = "query") String name,
            @RequestParam Integer itemId
    ) {
        return new ResultBean<>(nodeService.findByNameLike(name, itemId));
    }

}
