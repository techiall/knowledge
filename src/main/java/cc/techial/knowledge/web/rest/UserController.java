package cc.techial.knowledge.web.rest;

import cc.techial.knowledge.aop.authority.FlushAuthority;
import cc.techial.knowledge.beans.ResultBean;
import cc.techial.knowledge.domain.User;
import cc.techial.knowledge.repository.UserRepository;
import cc.techial.knowledge.security.UserPrincipal;
import cc.techial.knowledge.service.UserService;
import cc.techial.knowledge.service.dto.UserDTO;
import cc.techial.knowledge.service.mapper.UserMapper;
import cc.techial.knowledge.web.rest.vm.UserVM;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author techial
 */

@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserController(UserService userService,
                          UserRepository userRepository,
                          UserMapper userMapper) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @GetMapping("/me")
    public ResultBean me(@AuthenticationPrincipal Object object,
                         CsrfToken csrfToken) {
        final Map<String, Object> map = new HashMap<>(2);
        if (object instanceof UserPrincipal) {
            UserPrincipal userPrincipal = (UserPrincipal) object;
            final var user = userRepository.findById(userPrincipal.getId())
                                           .map(userMapper::toUserDTO)
                                           .orElse(new UserDTO());
            map.put("user", user);
        } else {
            map.put("user", new User());
        }
        map.put("_csrf", csrfToken);
        return ResultBean.ok(map);
    }

    @PutMapping("/me")
    public ResultBean update(@AuthenticationPrincipal UserPrincipal userPrincipal,
                             @RequestBody UserVM userVM) {
        final var user = userService.update(userPrincipal.getId(), userVM);
        return ResultBean.ok(userMapper.toUserDTO(user));
    }

    @PutMapping("/me/password")
    @FlushAuthority
    public ResultBean updatePassword(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                     String srcPassword,
                                     String password) {
        final var user = userService.updatePassword(userPrincipal.getId(), srcPassword, password);
        return ResultBean.ok(userMapper.toUserDTO(user));
    }

    @DeleteMapping("/me")
    @FlushAuthority
    public ResultBean deleteById(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        userService.deleteById(userPrincipal.getId());
        return ResultBean.ok();
    }
}
