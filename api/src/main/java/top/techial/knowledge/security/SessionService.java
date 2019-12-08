package top.techial.knowledge.security;

import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author techial
 */
@Service
@Log4j2
public class SessionService {
    private final SessionRegistry sessionRegistry;

    public SessionService(SessionRegistry sessionRegistry) {
        this.sessionRegistry = sessionRegistry;
    }

    public void flushAll() {
        SecurityContextHolder.clearContext();
    }

    public synchronized void flushId(String id) {
        List<Object> list = sessionRegistry.getAllPrincipals();
        log.debug(list);
        for (Object o : list) {
            if (!(o instanceof UserPrincipal)) {
                continue;
            }
            UserPrincipal userPrincipal = (UserPrincipal) o;
            if (userPrincipal.getId().equals(id)) {
                log.debug(userPrincipal);
                List<SessionInformation> allSessions = sessionRegistry.getAllSessions(userPrincipal, false);
                for (SessionInformation allSession : allSessions) {
                    log.debug("sessionId = {}, lastRequest = {}, principal = {}",
                        allSession.getSessionId(), allSession.getLastRequest(), allSession.getPrincipal());
                    allSession.expireNow();
                }
            }
        }

    }

    public List<Object> findAll() {
        return sessionRegistry.getAllPrincipals();
    }
}
