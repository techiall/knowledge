package top.techial.knowledge.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;
import top.techial.beans.ResultBean;
import top.techial.beans.ResultCode;
import top.techial.knowledge.domain.User;
import top.techial.knowledge.security.SessionService;
import top.techial.knowledge.security.UserPrincipal;
import top.techial.knowledge.service.KnowledgeNodeService;
import top.techial.knowledge.service.RecordService;
import top.techial.knowledge.service.UserService;

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
    private final KnowledgeNodeService knowledgeNodeService;
    private final PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
    private final SessionService sessionService;
    private final RecordService recordService;

    public UserController(UserService userService, KnowledgeNodeService knowledgeNodeService, SessionService sessionService, RecordService recordService) {
        this.userService = userService;
        this.knowledgeNodeService = knowledgeNodeService;
        this.sessionService = sessionService;
        this.recordService = recordService;
    }

    @GetMapping("/me")
    public ResultBean<Map<String, Object>> me(
        @AuthenticationPrincipal Object object,
        CsrfToken csrfToken
    ) {
        Map<String, Object> map = new HashMap<>(16);
        map.put("user", object);
        if (object instanceof UserPrincipal) {
            UserPrincipal userPrincipal = (UserPrincipal) object;
            User user = userService.findById(userPrincipal.getId()).orElse(null);
            if (log.isDebugEnabled()) {
                log.debug(sessionService.findAll());
            }
            map.put("me", user);
        }
        map.put("_csrf", csrfToken);
        return new ResultBean<>(map);
    }

    @PatchMapping("/{id}/nickName")
    public ResultBean<User> updateNickName(
        @AuthenticationPrincipal UserPrincipal userPrincipal,
        @PathVariable String id,
        String nickName
    ) {
        if (userPrincipal.getId().equals(id)) {
            User user = userService.findById(id).orElseThrow(NullPointerException::new);
            user.setNickName(nickName);
            userService.save(user);
            return new ResultBean<>(user);
        }
        return new ResultBean<>(ResultCode.CHECK_FAIL);
    }

    @PatchMapping("/{id}/password")
    public ResultBean<User> updatePassword(
        @AuthenticationPrincipal UserPrincipal userPrincipal,
        @PathVariable String id,
        String srcPassword,
        String password
    ) {
        if (userPrincipal.getId().equals(id)) {
            User user = userService.findById(id).orElseThrow(NullPointerException::new);
            if (!passwordEncoder.matches(srcPassword, user.getPassword())) {
                throw new IllegalArgumentException();
            }
        }

        User user = userService.updatePassword(id, password);
        sessionService.flushId(userPrincipal.getId());
        return new ResultBean<>(user);
    }

    @DeleteMapping("/{id}")
    public ResultBean<Object> deleteById(
        @AuthenticationPrincipal UserPrincipal userPrincipal,
        @PathVariable String id
    ) {
        sessionService.flushId(id);
        if (userPrincipal.getId().equals(id)) {
            userService.deleteById(userPrincipal.getId());
            knowledgeNodeService.deleteByUserId(userPrincipal.getId());
            recordService.deleteByUserId(userPrincipal.getId());
            return new ResultBean<>(true);
        }
        return new ResultBean<>(ResultCode.CHECK_FAIL);
    }
}
