package top.techial.knowledge.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;
import top.techial.knowledge.aspect.FlushAuthority;
import top.techial.knowledge.beans.ResultBean;
import top.techial.knowledge.domain.Item;
import top.techial.knowledge.domain.User;
import top.techial.knowledge.dto.UserDTO;
import top.techial.knowledge.exception.UserException;
import top.techial.knowledge.mapper.UserMapper;
import top.techial.knowledge.security.UserPrincipal;
import top.techial.knowledge.service.ItemService;
import top.techial.knowledge.service.NodeService;
import top.techial.knowledge.service.RecordService;
import top.techial.knowledge.service.UserService;
import top.techial.knowledge.vo.UserVO;

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
    private final NodeService nodeService;
    private final PasswordEncoder passwordEncoder;
    private final RecordService recordService;
    private final ItemService itemService;

    public UserController(
            UserService userService,
            NodeService nodeService,
            PasswordEncoder passwordEncoder,
            RecordService recordService,
            ItemService itemService
    ) {
        this.userService = userService;
        this.nodeService = nodeService;
        this.passwordEncoder = passwordEncoder;
        this.recordService = recordService;
        this.itemService = itemService;
    }

    @GetMapping("/me")
    public ResultBean<Map<String, Object>> me(@AuthenticationPrincipal Object object, CsrfToken csrfToken) {
        Map<String, Object> map = new HashMap<>(16);
        if (object instanceof UserPrincipal) {
            UserPrincipal userPrincipal = (UserPrincipal) object;
            User user = userService.findById(userPrincipal.getId()).orElse(new User());
            map.put("user", UserMapper.INSTANCE.toUserDTO(user));
        } else {
            map.put("user", new User());
        }
        map.put("_csrf", csrfToken);
        return new ResultBean<>(map);
    }

    @PatchMapping("/me")
    public ResultBean<UserDTO> update(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestBody UserVO userVO
    ) {
        User user = userService.findById(userPrincipal.getId())
                .orElseThrow(() -> new UserException(userPrincipal.getId()));
        if (userVO != null && userVO.getImage() != null && !userVO.getImage().isEmpty()) {
            user.setImages(userVO.getImage());
        }
        if (userVO != null && userVO.getNickName() != null && !userVO.getNickName().isEmpty()) {
            user.setNickName(userVO.getNickName());
        }
        user = userService.save(user);
        return new ResultBean<>(UserMapper.INSTANCE.toUserDTO(user));
    }

    @PatchMapping("/me/password")
    @FlushAuthority
    public ResultBean<UserDTO> updatePassword(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            String srcPassword,
            String password
    ) {
        User user = userService.findById(userPrincipal.getId())
                .orElseThrow(() -> new UserException(userPrincipal.getId()));

        if (!passwordEncoder.matches(srcPassword, user.getPassword())) {
            throw new IllegalArgumentException("password not match.");
        }

        userService.updatePassword(userPrincipal.getId(), password);
        return new ResultBean<>(UserMapper.INSTANCE.toUserDTO(user));
    }

    @DeleteMapping("/me")
    @FlushAuthority
    public ResultBean<Object> deleteById(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        List<Item> item = itemService.findByUserId(userPrincipal.getId());
        itemService.deleteByUserIdAndItemId(userPrincipal.getId(), item);
        itemService.deleteByUserId(userPrincipal.getId());
        item.parallelStream().map(Item::getId).forEach(nodeService::deleteByItemId);
        recordService.deleteByUserId(userPrincipal.getId());
        userService.deleteById(userPrincipal.getId());
        return new ResultBean<>();
    }
}
