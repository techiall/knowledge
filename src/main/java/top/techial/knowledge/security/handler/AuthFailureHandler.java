package top.techial.knowledge.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import top.techial.knowledge.web.rest.errors.ClientErrorException;
import top.techial.knowledge.web.rest.errors.UnauthorizedException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
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
        ClientErrorException clientErrorException;
        if (exception instanceof InternalAuthenticationServiceException && exception.getCause() instanceof ClientErrorException) {
            clientErrorException = (ClientErrorException) exception.getCause();
        } else {
            clientErrorException = new UnauthorizedException();
        }
        response.setStatus(clientErrorException.getCode());
        response.getWriter().write(objectMapper.writeValueAsString(clientErrorException.getResultBean()));
    }
}
