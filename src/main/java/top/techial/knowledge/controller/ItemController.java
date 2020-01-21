package top.techial.knowledge.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import top.techial.knowledge.beans.ResultBean;
import top.techial.knowledge.domain.Item;
import top.techial.knowledge.domain.Node;
import top.techial.knowledge.domain.User;
import top.techial.knowledge.dto.ItemDTO;
import top.techial.knowledge.exception.ItemException;
import top.techial.knowledge.mapper.ItemMapper;
import top.techial.knowledge.security.UserPrincipal;
import top.techial.knowledge.service.ItemService;
import top.techial.knowledge.service.NodeService;
import top.techial.knowledge.vo.ItemVO;

import javax.validation.Valid;
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
    @PreAuthorize("hasAnyAuthority(#id)")
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
    public ResultBean<ItemDTO> save(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @Valid @RequestBody ItemVO itemVo
    ) {
        Node node = new Node().setName("root");
        node = nodeService.saveItemRoot(node);
        Item item = ItemMapper.INSTANCE.toItem(itemVo)
                .setAuthor(new User().setId(userPrincipal.getId()))
                .setRootId(node.getId());
        itemService.insert(userPrincipal.getId(), item.getId());
        return new ResultBean<>(ItemMapper.INSTANCE.toItemDTO(itemService.save(item)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority(#id)")
    public ResultBean<ItemDTO> update(@Valid @RequestBody ItemVO itemVO, @PathVariable Integer id) {
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
        return new ResultBean<>(ItemMapper.INSTANCE.toItemDTO(itemService.save(item)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority(#id)")
    public ResultBean<Object> deleteById(@PathVariable Integer id, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        List<Item> item = itemService.findByUserId(userPrincipal.getId());
        List<SimpleGrantedAuthority> authority = ItemMapper.INSTANCE.toListSimpleGrantedAuthority(item);

        reSet(authority);

        itemService.deleteById(id);
        nodeService.deleteByItemId(id);
        return new ResultBean<>();
    }

    private static void reSet(List<SimpleGrantedAuthority> authority) {
        SecurityContext context = SecurityContextHolder.getContext();
        UserDetails userDetails = (UserDetails) context.getAuthentication().getPrincipal();
        Authentication auth = new UsernamePasswordAuthenticationToken(
                userDetails, userDetails.getPassword(), authority);
        context.setAuthentication(auth);
    }
}
