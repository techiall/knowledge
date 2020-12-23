package cc.techial.knowledge.web.rest;

import cc.techial.knowledge.beans.ResultBean;
import cc.techial.knowledge.repository.UserRepository;
import cc.techial.knowledge.service.UserService;
import cc.techial.knowledge.service.mapper.UserMapper;
import cc.techial.knowledge.web.rest.vm.RegisterVM;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author techial
 */
@RestController
@RequestMapping("/api/register")
public class RegisterController {
    private final UserRepository userRepository;
    private final UserService userService;
    private final UserMapper userMapper;

    public RegisterController(UserRepository userRepository,
                              UserService userService,
                              UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @GetMapping("/query")
    public ResultBean existsByUserName(@RequestParam String name) {
        return ResultBean.ok(userRepository.existsByUserName(name));
    }

    @PostMapping
    public ResultBean save(@Validated @RequestBody RegisterVM registerVM) {
        final var user = userMapper.toUser(registerVM);
        return ResultBean.ok(userService.saveNewUser(user));
    }
}
