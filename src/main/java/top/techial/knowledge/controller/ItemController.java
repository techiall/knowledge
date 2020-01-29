package top.techial.knowledge.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import top.techial.knowledge.aspect.FlushAuthority;
import top.techial.knowledge.beans.ResultBean;
import top.techial.knowledge.domain.Item;
import top.techial.knowledge.domain.Node;
import top.techial.knowledge.domain.User;
import top.techial.knowledge.dto.ItemDTO;
import top.techial.knowledge.dto.ItemShareDTO;
import top.techial.knowledge.exception.ItemException;
import top.techial.knowledge.mapper.ItemMapper;
import top.techial.knowledge.security.UserPrincipal;
import top.techial.knowledge.service.ItemService;
import top.techial.knowledge.service.NodeService;
import top.techial.knowledge.vo.ItemVO;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author techial
 */
@RestController
@RequestMapping("/api/item")
public class ItemController {
    private final ItemService itemService;
    private final NodeService nodeService;

    public ItemController(ItemService itemService, NodeService nodeService) {
        this.itemService = itemService;
        this.nodeService = nodeService;
    }

    @GetMapping("share")
    public ResultBean<List<ItemShareDTO>> share() {
        List<ItemShareDTO> list = itemService.findByShare(true)
                .parallelStream()
                .map(ItemMapper.INSTANCE::toItemShareDTO)
                .collect(Collectors.toList());
        return new ResultBean<>(list);
    }

    @GetMapping("/{id}")
    public ResultBean<ItemDTO> findById(@PathVariable Integer id) {
        Item item = itemService.findById(id).orElseThrow(() -> new ItemException(id));
        return new ResultBean<>(ItemMapper.INSTANCE.toItemDTO(item));
    }

    @GetMapping
    public ResultBean<List<ItemDTO>> findByUserId(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        return new ResultBean<>(itemService
                .findByUserId(userPrincipal.getId())
                .parallelStream()
                .map(ItemMapper.INSTANCE::toItemDTO)
                .collect(Collectors.toList())
        );
    }

    @PostMapping
    @FlushAuthority
    public ResultBean<ItemDTO> save(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestBody ItemVO itemVO
    ) {
        Node node = new Node().setName("root");
        node = nodeService.saveItemRoot(node);
        Item item = ItemMapper.INSTANCE.toItem(itemVO)
                .setAuthor(new User().setId(userPrincipal.getId()))
                .setRootNode(node);
        item = itemService.save(item);
        itemService.insert(userPrincipal.getId(), item.getId());

        return new ResultBean<>(ItemMapper.INSTANCE.toItemDTO(item));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ITEM_' + #id.toString())")
    public ResultBean<ItemDTO> update(@RequestBody ItemVO itemVO, @PathVariable Integer id) {
        Item item = itemService.findById(id).orElseThrow(() -> new ItemException(id));
        if (itemVO != null && itemVO.getDescription() != null) {
            item.setDescription(itemVO.getDescription());
        }
        if (itemVO != null && itemVO.getShare() != null) {
            item.setShare(itemVO.getShare());
        }
        if (itemVO != null && itemVO.getName() != null) {
            item.setName(itemVO.getName());
        }
        if (itemVO != null && itemVO.getImage() != null) {
            item.setImage(itemVO.getImage());
        }
        return new ResultBean<>(ItemMapper.INSTANCE.toItemDTO(itemService.save(item)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ITEM_' + #id.toString())")
    @FlushAuthority
    public ResultBean<Object> deleteById(@PathVariable Integer id, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        itemService.deleteByUserIdAndItemId(userPrincipal.getId(), id);
        itemService.deleteById(id);
        nodeService.deleteByItemId(id);

        return new ResultBean<>();
    }

}
