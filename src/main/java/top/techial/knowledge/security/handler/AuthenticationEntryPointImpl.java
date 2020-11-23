package top.techial.knowledge.security.handler;

import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import top.techial.knowledge.utils.JsonUtils;
import top.techial.knowledge.web.rest.errors.NotFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author techial
 */
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        final var e = new NotFoundException();
        response.setStatus(e.getCode());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        final var str = JsonUtils.writeValueAsString(e.getResultBean());
        response.getWriter().write(str == null ? "" : str);
    }
}
