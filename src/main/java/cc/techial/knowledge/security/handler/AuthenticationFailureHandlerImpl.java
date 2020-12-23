package cc.techial.knowledge.security.handler;

import cc.techial.knowledge.utils.JsonUtils;
import cc.techial.knowledge.web.rest.errors.ClientErrorException;
import cc.techial.knowledge.web.rest.errors.UnauthorizedException;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author techial
 */
public class AuthenticationFailureHandlerImpl implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception) throws IOException {
        final ClientErrorException clientErrorException;
        if (exception instanceof InternalAuthenticationServiceException && exception.getCause() instanceof ClientErrorException) {
            clientErrorException = (ClientErrorException) exception.getCause();
        } else {
            clientErrorException = new UnauthorizedException();
        }
        response.setStatus(clientErrorException.getCode());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        final var str = JsonUtils.writeValueAsString(clientErrorException.getResultBean());
        response.getWriter().write(str == null ? "" : str);
    }
}
