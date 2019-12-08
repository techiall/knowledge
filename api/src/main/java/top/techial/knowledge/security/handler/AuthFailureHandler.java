package top.techial.knowledge.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import top.techial.beans.ResultBean;
import top.techial.beans.ResultCode;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 验证失败 处理
 *
 * @author techial
 */
@Configuration
public class AuthFailureHandler implements AuthenticationFailureHandler {
    private final ObjectMapper objectMapper;

    public AuthFailureHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        if (exception instanceof DisabledException) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write(objectMapper.writeValueAsString(new ResultBean<>(ResultCode.ACCOUNT_DISABLE)));
        } else if (exception instanceof SessionAuthenticationException) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write(objectMapper.writeValueAsString(new ResultBean<>(ResultCode.MAXIMUM_SESSION)));
        } else {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write(objectMapper.writeValueAsString(new ResultBean<>(ResultCode.UNAUTHORIZED)));
        }
        response.setContentType(MediaType.APPLICATION_JSON_UTF8.toString());
    }
}
