package top.techial.knowledge.web.rest;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.techial.knowledge.beans.ResultBean;
import top.techial.knowledge.domain.User;
import top.techial.knowledge.repository.UserRepository;
import top.techial.knowledge.service.UserService;
import top.techial.knowledge.service.mapper.UserMapper;
import top.techial.knowledge.web.rest.vm.RegisterVM;

/**
 * @author techial
 */
@RestController
@RequestMapping("/api/register")
public class RegisterController {
    private final UserRepository userRepository;
    private final UserService userService;
    private final UserMapper userMapper;

    public RegisterController(
            UserRepository userRepository,
            UserService userService, UserMapper userMapper
    ) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @GetMapping("/query")
    public ResultBean<Boolean> existsByUserName(@RequestParam String name) {
        return ResultBean.ok(userRepository.existsByUserName(name));
    }

    @PostMapping
    public ResultBean<User> save(@Validated @RequestBody RegisterVM registerVM) {
        var user = userMapper.toUser(registerVM);
        return ResultBean.ok(userService.saveNewUser(user));
    }

}
