package top.techial.knowledge.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Service;
import top.techial.knowledge.security.UserPrincipal;

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

    public void flushId(UserPrincipal userPrincipal) {
        List<SessionInformation> allSessions = sessionRegistry.getAllSessions(userPrincipal, false);
        allSessions.forEach(allSession -> {
            if (log.isDebugEnabled()) {
                log.debug("sessionId: [{}], lastRequest: [{}], principal: [{}]",
                    allSession.getSessionId(), allSession.getLastRequest(), allSession.getPrincipal());
            }
            allSession.expireNow();
        });
    }

}
