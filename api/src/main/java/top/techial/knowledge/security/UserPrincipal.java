package top.techial.knowledge.security;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

/**
 * @author techial
 */
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Getter
public class UserPrincipal extends User {

    private final Integer id;

    public UserPrincipal(Integer id, String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.id = id;
    }
}
