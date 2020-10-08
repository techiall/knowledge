package top.techial.knowledge.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

/**
 * @author techial
 */
public class UserPrincipal extends User {

    private final Integer id;

    public UserPrincipal(Integer id,
                         String username,
                         String password,
                         Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.id = id;
    }

    public UserPrincipal(Integer id,
                         String username,
                         String password,
                         boolean enabled,
                         boolean accountNonExpired,
                         boolean credentialsNonExpired,
                         boolean accountNonLocked,
                         Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserPrincipal)) return false;
        if (!super.equals(o)) return false;

        UserPrincipal that = (UserPrincipal) o;

        return getId() != null ? getId().equals(that.getId()) : that.getId() == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (getId() != null ? getId().hashCode() : 0);
        return result;
    }
}
