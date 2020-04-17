package top.techial.knowledge.web.rest;

import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;
import top.techial.knowledge.aop.authority.FlushAuthority;
import top.techial.knowledge.beans.ResultBean;
import top.techial.knowledge.domain.Item;
import top.techial.knowledge.domain.User;
import top.techial.knowledge.repository.ItemRepository;
import top.techial.knowledge.repository.RecordRepository;
import top.techial.knowledge.repository.UserRepository;
import top.techial.knowledge.security.UserPrincipal;
import top.techial.knowledge.service.ItemService;
import top.techial.knowledge.service.NodeService;
import top.techial.knowledge.service.UserService;
import top.techial.knowledge.service.dto.UserDTO;
import top.techial.knowledge.service.mapper.UserMapper;
import top.techial.knowledge.web.rest.errors.PasswordNotMatchException;
import top.techial.knowledge.web.rest.errors.UserNotFoundException;
import top.techial.knowledge.web.rest.vm.UserVM;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author techial
 */

@RestController
@RequestMapping("/api/user")
@Log4j2
public class UserController {
    private final UserService userService;
    private final UserRepository userRepository;
    private final NodeService nodeService;
    private final ItemRepository itemRepository;
    private final PasswordEncoder passwordEncoder;
    private final RecordRepository recordRepository;
    private final UserMapper userMapper;
    private final ItemService itemService;

    public UserController(
            UserService userService,
            UserRepository userRepository,
            NodeService nodeService,
            ItemRepository itemRepository,
            PasswordEncoder passwordEncoder,
            RecordRepository recordRepository,
            UserMapper userMapper,
            ItemService itemService
    ) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.nodeService = nodeService;
        this.itemRepository = itemRepository;
        this.passwordEncoder = passwordEncoder;
        this.recordRepository = recordRepository;
        this.userMapper = userMapper;
        this.itemService = itemService;
    }

    @GetMapping("/me")
    public ResultBean<Map<String, Object>> me(@AuthenticationPrincipal Object object, CsrfToken csrfToken) {
        Map<String, Object> map = new HashMap<>(16);
        if (object instanceof UserPrincipal) {
            UserPrincipal userPrincipal = (UserPrincipal) object;
            User user = userRepository.findById(userPrincipal.getId()).orElse(new User());
            map.put("user", userMapper.toUserDTO(user));
        } else {
            map.put("user", new User());
        }
        map.put("_csrf", csrfToken);
        return ResultBean.ok(map);
    }

    @PatchMapping("/me")
    public ResultBean<UserDTO> update(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestBody UserVM userVM
    ) {
        User user = userRepository.findById(userPrincipal.getId())
                .orElseThrow(UserNotFoundException::new);
        if (userVM != null && userVM.getImage() != null && !userVM.getImage().isEmpty()) {
            user.setImages(userVM.getImage());
        }
        if (userVM != null && userVM.getNickName() != null && !userVM.getNickName().isEmpty()) {
            user.setNickName(userVM.getNickName());
        }
        user = userRepository.save(user);
        return ResultBean.ok(userMapper.toUserDTO(user));
    }

    @PatchMapping("/me/password")
    @FlushAuthority
    public ResultBean<UserDTO> updatePassword(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            String srcPassword,
            String password
    ) {
        User user = userRepository.findById(userPrincipal.getId())
                .orElseThrow(UserNotFoundException::new);

        if (!passwordEncoder.matches(srcPassword, user.getPassword())) {
            throw new PasswordNotMatchException();
        }

        userService.updatePassword(userPrincipal.getId(), password);
        return ResultBean.ok(userMapper.toUserDTO(user));
    }

    @DeleteMapping("/me")
    @FlushAuthority
    public ResultBean<Object> deleteById(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        List<Item> item = itemRepository.findAllByAuthorId(userPrincipal.getId());
        itemService.deleteByUserIdAndItemId(userPrincipal.getId(), item);
        itemRepository.deleteByAuthorId(userPrincipal.getId());
        item.parallelStream().map(Item::getId).forEach(nodeService::deleteByItemId);
        recordRepository.deleteByUserId(userPrincipal.getId());
        userRepository.deleteById(userPrincipal.getId());
        return ResultBean.ok();
    }
}
