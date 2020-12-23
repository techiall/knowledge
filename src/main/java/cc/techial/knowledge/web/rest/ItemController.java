package cc.techial.knowledge.web.rest;

import cc.techial.knowledge.aop.authority.FlushAuthority;
import cc.techial.knowledge.beans.ResultBean;
import cc.techial.knowledge.repository.ItemRepository;
import cc.techial.knowledge.security.UserPrincipal;
import cc.techial.knowledge.service.ItemService;
import cc.techial.knowledge.service.mapper.ItemMapper;
import cc.techial.knowledge.service.valid.Insert;
import cc.techial.knowledge.web.rest.errors.ItemNotFoundException;
import cc.techial.knowledge.web.rest.vm.ItemVM;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author techial
 */
@RestController
@RequestMapping("/api/item")
public class ItemController {
    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;
    private final ItemService itemService;

    public ItemController(ItemRepository itemRepository,
                          ItemMapper itemMapper,
                          ItemService itemService) {
        this.itemRepository = itemRepository;
        this.itemMapper = itemMapper;
        this.itemService = itemService;
    }

    @GetMapping("share")
    public ResultBean share(@PageableDefault Pageable pageable) {
        final var list = itemRepository.findByShare(true, pageable)
                                       .map(itemMapper::toItemDTO);
        return ResultBean.ok(list);
    }

    @GetMapping("/{id}")
    public ResultBean findById(@PathVariable int id) {
        return itemRepository.findById(id)
                             .map(itemMapper::toItemDTO)
                             .map(ResultBean::ok)
                             .orElseThrow(ItemNotFoundException::new);
    }

    @GetMapping
    public ResultBean findByUserId(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                   @PageableDefault Pageable pageable) {
        return ResultBean.ok(itemRepository.findAllByAuthorId(userPrincipal.getId(), pageable)
                                           .map(itemMapper::toItemDTO));
    }

    @PostMapping
    @FlushAuthority
    public ResultBean save(@AuthenticationPrincipal UserPrincipal userPrincipal,
                           @Validated(value = Insert.class) @RequestBody ItemVM itemVM) {
        if (itemVM.getName() == null || itemVM.getName().isEmpty()) {
            throw new IllegalArgumentException(String
                                                       .format("itemVO error. %s", itemVM.toString()));
        }
        final var _item = itemMapper.toItem(itemVM);
        final var item = itemService.save(userPrincipal.getId(), _item);
        return ResultBean.ok(itemMapper.toItemDTO(item));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ITEM_' + #id.toString())")
    public ResultBean update(@RequestBody ItemVM itemVM,
                             @PathVariable Integer id) {
        final var item = itemService.update(id, itemVM);
        return ResultBean.ok(itemMapper.toItemDTO(item));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ITEM_' + #id)")
    @FlushAuthority
    public ResultBean deleteById(@PathVariable int id,
                                 @AuthenticationPrincipal UserPrincipal userPrincipal) {
        itemService.deleteById(id, userPrincipal.getId());
        return ResultBean.ok();
    }
}
