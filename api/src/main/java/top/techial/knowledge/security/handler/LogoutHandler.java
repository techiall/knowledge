package top.techial.knowledge.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import top.techial.beans.ResultBean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 退出登录
 * 成功处理
 *
 * @author techial
 */
@Configuration
@Log4j2
public class LogoutHandler implements LogoutSuccessHandler {
    private final ObjectMapper objectMapper;

    public LogoutHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        response.getWriter().write(objectMapper.writeValueAsString(new ResultBean<>()));
        response.setContentType(MediaType.APPLICATION_JSON_UTF8.toString());
        response.setStatus(HttpStatus.OK.value());
        log.debug("logout success.");
    }
}
