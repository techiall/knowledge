package top.techial.knowledge.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import top.techial.knowledge.beans.ResultBean;
import top.techial.knowledge.domain.Labels;
import top.techial.knowledge.domain.Node;
import top.techial.knowledge.domain.Property;
import top.techial.knowledge.dto.NodeBaseDTO;
import top.techial.knowledge.dto.NodeInfoDTO;
import top.techial.knowledge.exception.ItemException;
import top.techial.knowledge.mapper.NodeMapper;
import top.techial.knowledge.security.UserPrincipal;
import top.techial.knowledge.service.ItemService;
import top.techial.knowledge.service.NodeService;
import top.techial.knowledge.service.RecordService;
import top.techial.knowledge.service.UserService;
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
    private final ItemService itemService;
    private final UserService userService;

    public NodeController(
            NodeService nodeService,
            RecordService recordService,
            ItemService itemService,
            UserService userService) {
        this.nodeService = nodeService;
        this.recordService = recordService;
        this.itemService = itemService;
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResultBean<NodeInfoDTO> findById(@PathVariable Long id) {
        Node node = nodeService.findById(id);
        return new ResultBean<>(NodeMapper.INSTANCE.toNodeInfoDTO(node));
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ITEM_' + #nodeVO.itemId.toString())")
    public ResultBean<NodeInfoDTO> save(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @Valid @RequestBody NodeVO nodeVO
    ) {
        Long itemId = itemService.findRootNodeId(nodeVO.getItemId())
                .orElseThrow(() -> new ItemException(nodeVO.getItemId()));
        Node node = nodeService.save(nodeVO, itemId);

        recordService.save(node.getId(), userPrincipal.getId(),
                nodeVO.getRecord().getOperator(), nodeVO.getRecord().getMessage());

        userService.resetAuthority(userPrincipal);

        return new ResultBean<>(NodeMapper.INSTANCE.toNodeInfoDTO(node));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ITEM_' + #nodeVO.itemId.toString()) AND hasAnyAuthority(#id)")
    public ResultBean<NodeInfoDTO> update(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long id,
            @Valid @RequestBody NodeVO nodeVO
    ) {
        Node node = nodeService.findById(id);

        if (nodeVO != null && nodeVO.getName() != null) {
            node.setName(nodeVO.getName());
            recordService.save(node.getId(), userPrincipal.getId(),
                    nodeVO.getRecord().getOperator(), nodeVO.getRecord().getMessage());
        }
        if (nodeVO != null && nodeVO.getLabels() != null) {
            node.setLabels(new Labels().setLabels(nodeVO.getLabels()));
            recordService.save(node.getId(), userPrincipal.getId(),
                    nodeVO.getRecord().getOperator(), nodeVO.getRecord().getMessage());
        }
        if (nodeVO != null && nodeVO.getProperty() != null) {
            node.setProperty(new Property().setProperty(nodeVO.getProperty()));
            recordService.save(node.getId(), userPrincipal.getId(),
                    nodeVO.getRecord().getOperator(), nodeVO.getRecord().getMessage());
        }

        node = nodeService.save(node);

        return new ResultBean<>(NodeMapper.INSTANCE.toNodeInfoDTO(node));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ITEM_' + #itemId.toString()) AND hasAnyAuthority(#id)")
    public ResultBean<Boolean> deleteById(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestParam Integer itemId,
            @PathVariable Long id
    ) {
        nodeService.deleteIdAndRelationship(id);
        recordService.deleteByNodeId(id);

        userService.resetAuthority(userPrincipal);

        return new ResultBean<>(true);
    }

    @DeleteMapping
    @PreAuthorize("hasAnyAuthority('ITEM_' + #itemId.toString()) AND hasAnyAuthority(#ids)")
    public ResultBean<Boolean> deleteByIds(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestBody Set<Long> ids,
            @RequestParam Integer itemId
    ) {
        nodeService.deleteIdsAndRelationship(ids);
        recordService.deleteByNodeIds(ids);

        userService.resetAuthority(userPrincipal);

        return new ResultBean<>(true);
    }

    @GetMapping
    public ResultBean<List<NodeBaseDTO>> findByRootNode(
            @RequestParam Integer itemId,
            @RequestParam(defaultValue = "1") Integer depth
    ) {
        Long rootNodeId = itemService.findRootNodeId(itemId)
                .orElseThrow(() -> new ItemException(itemId));
        return new ResultBean<>(nodeService.findByChildNode(rootNodeId, depth));
    }

    @GetMapping("/{id}/graph")
    public ResultBean<Object> findByIdGraph(@PathVariable Long id) {
//        return new ResultBean<>(knowledgeNodeService.findByIdGraph(id));
        return null;
    }

    @GetMapping("/{id}/link")
    public ResultBean<Map<String, List<NodeBaseDTO>>> getChildAndParent(
            @PathVariable Long id,
            @RequestParam(required = false, defaultValue = "1") int depth
    ) {
        return new ResultBean<>(nodeService.getChildAndParent(id, depth));
    }

    @GetMapping("/{id}/child")
    public ResultBean<List<NodeBaseDTO>> findChildNode(
            @PathVariable Long id,
            @RequestParam(required = false, defaultValue = "1") int depth
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
