package top.techial.knowledge.security;

import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import top.techial.knowledge.domain.Item;
import top.techial.knowledge.domain.User;
import top.techial.knowledge.mapper.UserMapper;
import top.techial.knowledge.service.NodeService;
import top.techial.knowledge.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author techial
 */
@Service
@Log4j2
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserService userService;

    private final NodeService nodeService;

    public UserDetailsServiceImpl(UserService userService, NodeService nodeService) {
        this.userService = userService;
        this.nodeService = nodeService;
    }

    @Override
    public UserDetails loadUserByUsername(String s) {
        User user = userService.findByUserName(s)
                .orElseThrow(() -> new UsernameNotFoundException(
                        String.format("userName:[%s] not found.", s)));
        List<Long> nodes = nodeService.findByItemIds(user
                .getItem()
                .parallelStream()
                .map(Item::getId)
                .collect(Collectors.toList()));
        return UserMapper.INSTANCE.toUserPrincipal(user, nodes);
    }

}
