package top.techial.knowledge.web.rest;

import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;
import top.techial.knowledge.aop.authority.FlushAuthority;
import top.techial.knowledge.beans.ResultBean;
import top.techial.knowledge.domain.User;
import top.techial.knowledge.repository.UserRepository;
import top.techial.knowledge.security.UserPrincipal;
import top.techial.knowledge.service.UserService;
import top.techial.knowledge.service.dto.UserDTO;
import top.techial.knowledge.service.mapper.UserMapper;
import top.techial.knowledge.web.rest.vm.UserVM;

import java.util.HashMap;
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
    private final UserMapper userMapper;

    public UserController(
            UserService userService,
            UserRepository userRepository,
            UserMapper userMapper
    ) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.userMapper = userMapper;
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
        User user = userService.update(userPrincipal.getId(), userVM);
        return ResultBean.ok(userMapper.toUserDTO(user));
    }

    @PatchMapping("/me/password")
    @FlushAuthority
    public ResultBean<UserDTO> updatePassword(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            String srcPassword,
            String password
    ) {
        User user = userService.updatePassword(userPrincipal.getId(), srcPassword, password);
        return ResultBean.ok(userMapper.toUserDTO(user));
    }

    @DeleteMapping("/me")
    @FlushAuthority
    public ResultBean<Object> deleteById(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        userService.deleteById(userPrincipal.getId());
        return ResultBean.ok();
    }
}
