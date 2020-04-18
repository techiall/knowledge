package top.techial.knowledge.web.rest;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.techial.knowledge.aop.authority.FlushAuthority;
import top.techial.knowledge.beans.ResultBean;
import top.techial.knowledge.domain.Node;
import top.techial.knowledge.repository.ItemRepository;
import top.techial.knowledge.repository.RecordRepository;
import top.techial.knowledge.repository.search.NodeSearchRepository;
import top.techial.knowledge.security.UserPrincipal;
import top.techial.knowledge.service.NodeService;
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
    private final ItemRepository itemRepository;
    private final NodeMapper nodeMapper;
    private final RecordRepository recordRepository;
    private final NodeSearchRepository nodeSearchRepository;

    public NodeController(
            NodeService nodeService,
            ItemRepository itemRepository,
            NodeMapper nodeMapper,
            RecordRepository recordRepository,
            NodeSearchRepository nodeSearchRepository
    ) {
        this.nodeService = nodeService;
        this.itemRepository = itemRepository;
        this.nodeMapper = nodeMapper;
        this.recordRepository = recordRepository;
        this.nodeSearchRepository = nodeSearchRepository;
    }

    @GetMapping("/{id}")
    public ResultBean<NodeInfoDTO> findById(@PathVariable Long id) {
        Node node = nodeService.findById(id);
        return ResultBean.ok(nodeMapper.toNodeInfoDTO(node));
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ITEM_' + #nodeVM.itemId.toString())")
    @FlushAuthority
    public ResultBean<Map<String, Object>> save(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @Validated(value = Insert.class) @RequestBody NodeVM nodeVM
    ) {
        return ResultBean.ok(nodeService.save(userPrincipal.getId(), nodeVM));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ITEM_' + #nodeVM.itemId.toString()) AND hasAnyAuthority(#id)")
    public ResultBean<NodeInfoDTO> update(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long id,
            @Validated(value = Update.class) @RequestBody NodeVM nodeVM
    ) {
        Node node = nodeService.update(id, nodeVM, userPrincipal.getId());
        return ResultBean.ok(nodeMapper.toNodeInfoDTO(node));
    }

    @PutMapping("/{id}/movement")
    @PreAuthorize("hasAnyAuthority(#target) AND hasAnyAuthority(#id)")
    public ResultBean<Boolean> move(@PathVariable Long id, @RequestParam Long target) {
        if (Objects.equals(id, target)) {
            throw new IllegalArgumentException(String.format("id: [%s], target: [%s]", id, target));
        }
        nodeService.move(id, target);
        return ResultBean.ok(true);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ITEM_' + #itemId.toString()) AND hasAnyAuthority(#id)")
    @FlushAuthority
    public ResultBean<Boolean> deleteById(
            @RequestParam Integer itemId,
            @PathVariable Long id
    ) {
        nodeService.deleteById(id);
        return ResultBean.ok(true);
    }

    @DeleteMapping
    @PreAuthorize("hasAnyAuthority('ITEM_' + #itemId.toString()) AND hasAnyAuthority(#ids)")
    @FlushAuthority
    public ResultBean<Boolean> deleteByIds(
            @RequestBody Set<Long> ids,
            @RequestParam Integer itemId
    ) {
        nodeService.deleteIdsAndRelationship(ids);
        nodeSearchRepository.deleteByIdIn(ids);
        recordRepository.deleteByNodeIdIn(ids);

        return ResultBean.ok(true);
    }

    @GetMapping
    public ResultBean<List<NodeBaseDTO>> findByRootNode(
            @RequestParam Integer itemId,
            @RequestParam(defaultValue = "1") Integer depth
    ) {
        Long rootNodeId = itemRepository.findRootNodeId(itemId)
                .orElseThrow(ItemNotFoundException::new);
        return ResultBean.ok(nodeService.findByChildNode(rootNodeId, depth));
    }

    @GetMapping("/{id}/graph")
    public ResultBean<Map<String, Object>> findByIdGraph(@PathVariable Long id) {
        return ResultBean.ok(nodeService.findByIdGraph(id));
    }

    @GetMapping("/{id}/link")
    public ResultBean<Map<String, Object>> getChildAndParent(@PathVariable Long id) {
        return ResultBean.ok(nodeService.getChildAndParent(id));
    }

    @GetMapping("/{id}/child")
    public ResultBean<List<NodeBaseDTO>> findChildNode(
            @PathVariable Long id,
            @RequestParam(required = false, defaultValue = "1") int depth
    ) {
        return ResultBean.ok(nodeService.findByChildNode(id, depth));
    }

    @GetMapping("/name")
    public ResultBean<List<NodeInfoDTO>> findByName(
            @RequestParam(name = "query") String name,
            @RequestParam Integer itemId
    ) {
        return ResultBean.ok(nodeService.findByNameLike(name, itemId));
    }

}
