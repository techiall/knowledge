package top.techial.knowledge.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import top.techial.beans.ResultBean;
import top.techial.knowledge.domain.User;
import top.techial.knowledge.service.UserService;
import top.techial.knowledge.vo.RegisterVO;

/**
 * @author techial
 */
@RestController
@RequestMapping("/api/register")
@Log4j2
public class RegisterController {
    private final UserService userService;

    private final PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    public RegisterController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/query")
    public ResultBean<Object> existsByUserName(@RequestParam String name) {
        return new ResultBean<>(userService.existsByUserName(name));
    }

    @PostMapping
    public ResultBean<User> save(@RequestBody RegisterVO registerVO) {
        User user = registerVO.toUser();
        user.setPassword(passwordEncoder.encode(registerVO.getPassword()));
        return new ResultBean<>(userService.save(user));
    }

}
