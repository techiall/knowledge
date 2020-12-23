package cc.techial.knowledge.security;

import cc.techial.knowledge.domain.Item;
import cc.techial.knowledge.domain.User;
import cc.techial.knowledge.repository.NodeRepository;
import cc.techial.knowledge.repository.UserRepository;
import cc.techial.knowledge.service.mapper.ItemMapper;
import cc.techial.knowledge.service.mapper.NodeMapper;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author techial
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final NodeRepository nodeRepository;
    private final ItemMapper itemMapper;
    private final NodeMapper nodeMapper;
    private final UserRepository userRepository;

    public UserDetailsServiceImpl(NodeRepository nodeRepository,
                                  ItemMapper itemMapper,
                                  NodeMapper nodeMapper,
                                  UserRepository userRepository) {
        this.nodeRepository = nodeRepository;
        this.itemMapper = itemMapper;
        this.nodeMapper = nodeMapper;
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String s) {
        User user = userRepository.findFirstByUserName(s)
                                  .orElseThrow(() -> new UsernameNotFoundException(String.format("userName:[%s] not found.", s)));

        if (user.getItem() == null || user.getItem().isEmpty()) {
            return toUserPrincipal(user, Collections.emptyList());
        }

        List<Long> nodes = nodeRepository.findByItemIdIn(user.getItem()
                                                             .parallelStream()
                                                             .map(Item::getId)
                                                             .collect(Collectors.toList()));
        return toUserPrincipal(user, nodes);
    }

    private UserPrincipal toUserPrincipal(User user, List<Long> node) {
        List<SimpleGrantedAuthority> authorities = itemMapper.toListSimpleGrantedAuthority(
                user.getItem().parallelStream().map(Item::getId).collect(Collectors.toList()));
        List<SimpleGrantedAuthority> nodes = nodeMapper.toListSimpleGrantedAuthority(node);
        authorities.addAll(nodes);
        return new UserPrincipal(
                user.getId(),
                user.getUserName(),
                user.getPassword(),
                user.getEnabled(),
                user.getAccountNonExpired(),
                user.getCredentialsNonExpired(),
                user.getAccountNonLocked(),
                new HashSet<>(authorities)
        );
    }
}
