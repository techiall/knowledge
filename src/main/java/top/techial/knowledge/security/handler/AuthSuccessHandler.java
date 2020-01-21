package top.techial.knowledge.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import top.techial.knowledge.beans.ResultBean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 验证成功 处理
 *
 * @author techial
 */
@Configuration
public class AuthSuccessHandler implements AuthenticationSuccessHandler {
    private final ObjectMapper objectMapper;

    public AuthSuccessHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        response.getWriter().write(objectMapper.writeValueAsString(new ResultBean<>()));
        response.setContentType(MediaType.APPLICATION_JSON_UTF8.toString());
        response.setStatus(HttpStatus.OK.value());
    }
}
