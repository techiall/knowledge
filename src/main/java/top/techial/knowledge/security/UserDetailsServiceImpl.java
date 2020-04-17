package top.techial.knowledge.security;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import top.techial.knowledge.domain.Item;
import top.techial.knowledge.domain.User;
import top.techial.knowledge.repository.UserRepository;
import top.techial.knowledge.service.NodeService;
import top.techial.knowledge.service.mapper.ItemMapper;
import top.techial.knowledge.service.mapper.NodeMapper;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author techial
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final NodeService nodeService;
    private final ItemMapper itemMapper;
    private final NodeMapper nodeMapper;
    private final UserRepository userRepository;

    public UserDetailsServiceImpl(
            NodeService nodeService,
            ItemMapper itemMapper,
            NodeMapper nodeMapper,
            UserRepository userRepository
    ) {
        this.nodeService = nodeService;
        this.itemMapper = itemMapper;
        this.nodeMapper = nodeMapper;
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String s) {
        User user = userRepository.findFirstByUserName(s)
                .orElseThrow(() -> new UsernameNotFoundException(
                        String.format("userName:[%s] not found.", s)));
        List<Long> nodes;
        if (user.getItem() == null || user.getItem().isEmpty()) {
            nodes = Collections.emptyList();
        } else {
            nodes = nodeService.findByItemIds(user
                    .getItem()
                    .parallelStream()
                    .map(Item::getId)
                    .collect(Collectors.toList()));
        }
        return toUserPrincipal(user, nodes);
    }

    private UserPrincipal toUserPrincipal(User user, List<Long> node) {
        List<SimpleGrantedAuthority> authorities = itemMapper.toListSimpleGrantedAuthority(user.getItem());
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
