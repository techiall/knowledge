package top.techial.knowledge.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import top.techial.beans.ResultBean;
import top.techial.beans.ResultCode;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author techial
 */
@Configuration
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {
    private final ObjectMapper objectMapper;

    public AuthenticationEntryPointImpl(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setStatus(HttpStatus.NOT_FOUND.value());
        response.getWriter().write(objectMapper.writeValueAsString(new ResultBean<>(ResultCode.CHECK_FAIL)));
        response.setContentType(MediaType.APPLICATION_JSON_UTF8.toString());
    }
}
