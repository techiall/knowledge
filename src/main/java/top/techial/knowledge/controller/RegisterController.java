package top.techial.knowledge.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.techial.knowledge.beans.ResultBean;
import top.techial.knowledge.domain.User;
import top.techial.knowledge.exception.UsernameIsRegisterException;
import top.techial.knowledge.mapper.UserMapper;
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

    private final PasswordEncoder passwordEncoder;

    public RegisterController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/query")
    public ResultBean<Boolean> existsByUserName(@RequestParam String name) {
        return new ResultBean<>(userService.existsByUserName(name));
    }

    @PostMapping
    public ResultBean<User> save(@Validated @RequestBody RegisterVO registerVO) {
        if (userService.existsByUserName(registerVO.getUserName())) {
            throw new UsernameIsRegisterException(registerVO.getUserName());
        }
        User user = UserMapper.INSTANCE.toUser(registerVO);
        user.setPassword(passwordEncoder.encode(registerVO.getPassword()));
        return new ResultBean<>(userService.save(user));
    }

}
