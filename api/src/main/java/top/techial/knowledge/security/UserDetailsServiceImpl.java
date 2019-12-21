package top.techial.knowledge.security;

import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import top.techial.knowledge.domain.User;
import top.techial.knowledge.service.UserService;

/**
 * @author techial
 */
@Service
@Log4j2
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserService userService;

    public UserDetailsServiceImpl(UserService userService) {
        this.userService = userService;
    }


    @Override
    public UserDetails loadUserByUsername(String s) {
        User user = userService.findByUserName(s)
            .orElseThrow(() -> new UsernameNotFoundException("user not found."));
        if (log.isDebugEnabled()) {
            log.debug(user.getId());
        }
        return user.toUserPrincipal();
    }

}
