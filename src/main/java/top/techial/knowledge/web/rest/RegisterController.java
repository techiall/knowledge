package top.techial.knowledge.web.rest;

import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.techial.knowledge.beans.ResultBean;
import top.techial.knowledge.domain.User;
import top.techial.knowledge.repository.UserRepository;
import top.techial.knowledge.service.mapper.UserMapper;
import top.techial.knowledge.web.rest.errors.UsernameIsRegisterException;
import top.techial.knowledge.web.rest.vm.RegisterVM;

/**
 * @author techial
 */
@RestController
@RequestMapping("/api/register")
@Log4j2
public class RegisterController {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public RegisterController(
            UserRepository userRepository,
            UserMapper userMapper,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/query")
    public ResultBean<Boolean> existsByUserName(@RequestParam String name) {
        return ResultBean.ok(userRepository.existsByUserName(name));
    }

    @PostMapping
    public ResultBean<User> save(@Validated @RequestBody RegisterVM registerVM) {
        if (userRepository.existsByUserName(registerVM.getUserName())) {
            throw new UsernameIsRegisterException();
        }
        User user = userMapper.toUser(registerVM);
        user.setPassword(passwordEncoder.encode(registerVM.getPassword()));
        return ResultBean.ok(userRepository.save(user));
    }

}
