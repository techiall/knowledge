package cc.techial.knowledge.security.handler;

import cc.techial.knowledge.utils.JsonUtils;
import cc.techial.knowledge.web.rest.errors.ForbiddenException;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author techial
 */
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {
        final var e = new ForbiddenException();
        response.setStatus(e.getCode());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        final var str = JsonUtils.writeValueAsString(e.getResultBean());
        response.getWriter().write(str == null ? "" : str);
    }
}
