package top.techial.knowledge.web.rest;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.techial.knowledge.aop.authority.FlushAuthority;
import top.techial.knowledge.beans.ResultBean;
import top.techial.knowledge.domain.Labels;
import top.techial.knowledge.domain.Node;
import top.techial.knowledge.domain.Property;
import top.techial.knowledge.security.UserPrincipal;
import top.techial.knowledge.service.ItemService;
import top.techial.knowledge.service.NodeService;
import top.techial.knowledge.service.RecordService;
import top.techial.knowledge.service.dto.NodeBaseDTO;
import top.techial.knowledge.service.dto.NodeInfoDTO;
import top.techial.knowledge.service.mapper.NodeMapper;
import top.techial.knowledge.service.valid.Insert;
import top.techial.knowledge.service.valid.Update;
import top.techial.knowledge.web.rest.errors.ItemNotFoundException;
import top.techial.knowledge.web.rest.vm.NodeVM;

import java.util.List;
import java.util.Map;
import java.util.Objects;
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

    public NodeController(
            NodeService nodeService,
            RecordService recordService,
            ItemService itemService
    ) {
        this.nodeService = nodeService;
        this.recordService = recordService;
        this.itemService = itemService;
    }

    @GetMapping("/{id}")
    public ResultBean<NodeInfoDTO> findById(@PathVariable Long id) {
        Node node = nodeService.findById(id);
        return new ResultBean<>(NodeMapper.INSTANCE.toNodeInfoDTO(node));
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ITEM_' + #nodeVM.itemId.toString())")
    @FlushAuthority
    public ResultBean<NodeInfoDTO> save(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @Validated(value = Insert.class) @RequestBody NodeVM nodeVM
    ) {
        Long itemId = itemService.findRootNodeId(nodeVM.getItemId())
                .orElseThrow(ItemNotFoundException::new);
        Node node = nodeService.findByItemIdAndName(nodeVM.getName().trim(), nodeVM.getItemId())
                .orElse(null);
        if (node == null) {
            node = nodeService.save(nodeVM, itemId);
            recordService.save(node.getId(), userPrincipal.getId(),
                    nodeVM.getRecord().getOperator(), nodeVM.getRecord().getMessage());
        }
        return new ResultBean<>(NodeMapper.INSTANCE.toNodeInfoDTO(node));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ITEM_' + #nodeVM.itemId.toString()) AND hasAnyAuthority(#id)")
    public ResultBean<NodeInfoDTO> update(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long id,
            @Validated(value = Update.class) @RequestBody NodeVM nodeVM
    ) {
        Node node = nodeService.findById(id);

        if (nodeVM != null && nodeVM.getName() != null && !nodeVM.getName().isEmpty()) {
            node.setName(nodeVM.getName());
            recordService.save(node.getId(), userPrincipal.getId(),
                    nodeVM.getRecord().getOperator(), nodeVM.getRecord().getMessage());
        }
        if (nodeVM != null && nodeVM.getLabels() != null) {
            node.setLabels(new Labels().setLabels(nodeVM.getLabels()));
            recordService.save(node.getId(), userPrincipal.getId(),
                    nodeVM.getRecord().getOperator(), nodeVM.getRecord().getMessage());
        }

        if (nodeVM != null && nodeVM.getProperty() != null) {
            node.setProperty(new Property().setProperty(nodeService.buildProperty(nodeVM.getProperty())));
            recordService.save(node.getId(), userPrincipal.getId(),
                    nodeVM.getRecord().getOperator(), nodeVM.getRecord().getMessage());
        }

        node = nodeService.save(node);

        return new ResultBean<>(NodeMapper.INSTANCE.toNodeInfoDTO(node));
    }

    @PutMapping("/{id}/movement")
    @PreAuthorize("hasAnyAuthority(#target) AND hasAnyAuthority(#id)")
    public ResultBean<Boolean> move(@PathVariable Long id, @RequestParam Long target) {
        if (Objects.equals(id, target)) {
            throw new IllegalArgumentException(String.format("id: [%s], target: [%s]", id, target));
        }
        nodeService.move(id, target);
        return new ResultBean<>(true);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ITEM_' + #itemId.toString()) AND hasAnyAuthority(#id)")
    @FlushAuthority
    public ResultBean<Boolean> deleteById(
            @RequestParam Integer itemId,
            @PathVariable Long id
    ) {
        nodeService.deleteIdAndRelationship(id);
        recordService.deleteByNodeId(id);

        return new ResultBean<>(true);
    }

    @DeleteMapping
    @PreAuthorize("hasAnyAuthority('ITEM_' + #itemId.toString()) AND hasAnyAuthority(#ids)")
    @FlushAuthority
    public ResultBean<Boolean> deleteByIds(
            @RequestBody Set<Long> ids,
            @RequestParam Integer itemId
    ) {
        nodeService.deleteIdsAndRelationship(ids);
        recordService.deleteByNodeIds(ids);

        return new ResultBean<>(true);
    }

    @GetMapping
    public ResultBean<List<NodeBaseDTO>> findByRootNode(
            @RequestParam Integer itemId,
            @RequestParam(defaultValue = "1") Integer depth
    ) {
        Long rootNodeId = itemService.findRootNodeId(itemId)
                .orElseThrow(ItemNotFoundException::new);
        return new ResultBean<>(nodeService.findByChildNode(rootNodeId, depth));
    }

    @GetMapping("/{id}/graph")
    public ResultBean<Map<String, Object>> findByIdGraph(@PathVariable Long id) {
        return new ResultBean<>(nodeService.findByIdGraph(id));
    }

    @GetMapping("/{id}/link")
    public ResultBean<Map<String, Object>> getChildAndParent(@PathVariable Long id) {
        return new ResultBean<>(nodeService.getChildAndParent(id));
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
