package top.techial.knowledge.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.lang.Nullable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;
import top.techial.beans.ResultBean;
import top.techial.beans.ResultCode;
import top.techial.knowledge.domain.User;
import top.techial.knowledge.exception.UserException;
import top.techial.knowledge.security.UserPrincipal;
import top.techial.knowledge.service.KnowledgeNodeService;
import top.techial.knowledge.service.RecordService;
import top.techial.knowledge.service.SessionService;
import top.techial.knowledge.service.UserService;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author techial
 */

@RestController
@RequestMapping("/api/user")
@Log4j2
public class UserController {
    private final UserService userService;
    private final KnowledgeNodeService knowledgeNodeService;
    private final PasswordEncoder passwordEncoder;
    private final SessionService sessionService;
    private final RecordService recordService;

    public UserController(
        UserService userService,
        KnowledgeNodeService knowledgeNodeService,
        PasswordEncoder passwordEncoder,
        SessionService sessionService,
        RecordService recordService
    ) {
        this.userService = userService;
        this.knowledgeNodeService = knowledgeNodeService;
        this.passwordEncoder = passwordEncoder;
        this.sessionService = sessionService;
        this.recordService = recordService;
    }

    @GetMapping("/me")
    public ResultBean<Map<String, Object>> me(@AuthenticationPrincipal Object object, CsrfToken csrfToken) {
        Map<String, Object> map = new HashMap<>(16);
        map.put("user", object);
        if (object instanceof UserPrincipal) {
            UserPrincipal userPrincipal = (UserPrincipal) object;
            User user = userService.findById(userPrincipal.getId()).orElse(null);
            map.put("me", user);
        }
        map.put("_csrf", csrfToken);
        return new ResultBean<>(map);
    }

    @PatchMapping("/{id}/nickName")
    public ResultBean<User> updateNickName(
        @AuthenticationPrincipal UserPrincipal userPrincipal,
        @PathVariable Integer id,
        @Nullable String nickName
    ) {
        if (userPrincipal.getId().equals(id)) {
            User user = userService.findById(id).orElseThrow(() -> new UserException(id));
            nickName = nickName == null ? "" : nickName;
            user.setNickName(nickName);
            userService.save(user);
            return new ResultBean<>(user);
        }
        return new ResultBean<>(ResultCode.CHECK_FAIL);
    }

    @PatchMapping("/{id}/password")
    public ResultBean<User> updatePassword(
        @AuthenticationPrincipal UserPrincipal userPrincipal,
        @PathVariable Integer id,
        String srcPassword,
        String password
    ) {
        User user = userService.findById(id).orElseThrow(() -> new UserException(id));
        if (Objects.equals(user.getId(), id) && !passwordEncoder.matches(srcPassword, user.getPassword())) {
            throw new IllegalArgumentException("password not match.");
        }

        userService.updatePassword(id, password);
        sessionService.flushId(userPrincipal);
        return new ResultBean<>(user);
    }

    @DeleteMapping("/{id}")
    public ResultBean<Object> deleteById(
        @AuthenticationPrincipal UserPrincipal userPrincipal,
        @PathVariable Integer id
    ) {
        sessionService.flushId(userPrincipal);
        if (Objects.equals(userPrincipal.getId(), id)) {
            userService.deleteById(userPrincipal.getId());
            knowledgeNodeService.deleteByUserId(userPrincipal.getId());
            recordService.deleteByUserId(userPrincipal.getId());
            return new ResultBean<>(true);
        }
        return new ResultBean<>(ResultCode.CHECK_FAIL);
    }
}
