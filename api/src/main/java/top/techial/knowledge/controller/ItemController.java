package top.techial.knowledge.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import top.techial.beans.ResultBean;
import top.techial.knowledge.domain.Item;
import top.techial.knowledge.domain.User;
import top.techial.knowledge.dto.ItemDTO;
import top.techial.knowledge.exception.ItemException;
import top.techial.knowledge.exception.UserNotFoundItemException;
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

    @GetMapping("/{id}")
    public ResultBean<Item> findById(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Integer id
    ) {
        if (itemService.checkByIdAndUserId(id, userPrincipal.getId())) {
            throw new UserNotFoundItemException(userPrincipal.getId(), id);
        }
        return new ResultBean<>(itemService.findById(id).orElseThrow(() -> new ItemException(id)));
    }

    @GetMapping
    public ResultBean<List<ItemDTO>> findByUserId(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestParam Integer userId
    ) {
        return new ResultBean<>(itemService
                .findByUserId(userId)
                .parallelStream()
                .map(ItemMapper.INSTANCE::toItemDTO)
                .collect(Collectors.toList())
        );
    }

    @PostMapping
    public ResultBean<ItemDTO> save(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestBody ItemVO itemVo
    ) {
        Item item = ItemMapper.INSTANCE.toItem(itemVo).setAuthor(new User().setId(userPrincipal.getId()));
        return new ResultBean<>(ItemMapper.INSTANCE.toItemDTO(itemService.save(item)));
    }

    @PutMapping("/{id}")
    public ResultBean<ItemDTO> update(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestBody ItemVO itemVo,
            @PathVariable Integer id
    ) {
        if (itemService.checkByIdAndUserId(id, userPrincipal.getId())) {
            throw new UserNotFoundItemException(userPrincipal.getId(), id);
        }
        Item item = ItemMapper.INSTANCE.toItem(itemVo).setAuthor(new User().setId(userPrincipal.getId()));
        item.setId(id);
        return new ResultBean<>(ItemMapper.INSTANCE.toItemDTO(itemService.save(item)));
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Integer id, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        if (itemService.checkByIdAndUserId(id, userPrincipal.getId())) {
            throw new UserNotFoundItemException(userPrincipal.getId(), id);
        }
        itemService.deleteById(id);
        nodeService.deleteByItemId(id);
    }
}
