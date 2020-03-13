package top.techial.knowledge.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.techial.knowledge.domain.Item;
import top.techial.knowledge.domain.User;
import top.techial.knowledge.repository.ItemRepository;
import top.techial.knowledge.repository.UserRepository;
import top.techial.knowledge.security.UserPrincipal;
import top.techial.knowledge.service.mapper.ItemMapper;
import top.techial.knowledge.service.mapper.NodeMapper;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author techial
 */
@Service
@Log4j2
@CacheConfig(cacheNames = "common")
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ItemRepository itemRepository;
    private final NodeService nodeService;
    private final NodeMapper nodeMapper;
    private final ItemMapper itemMapper;

    public UserService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            ItemRepository itemRepository,
            NodeService nodeService,
            NodeMapper nodeMapper,
            ItemMapper itemMapper
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.itemRepository = itemRepository;
        this.nodeService = nodeService;
        this.nodeMapper = nodeMapper;
        this.itemMapper = itemMapper;
    }

    @Cacheable(key = "#root.targetClass.simpleName + #root.methodName + #p0", unless = "#result == null")
    public Optional<User> findByUserName(String userName) {
        return userRepository.findFirstByUserName(userName);
    }

    @Cacheable(key = "#root.targetClass.simpleName + #root.methodName + #p0", unless = "#result == null")
    public Optional<User> findById(Integer id) {
        return userRepository.findById(id);
    }

    @CacheEvict(allEntries = true)
    @Transactional
    public void updatePassword(Integer id, String password) {
        userRepository.updatePassword(id, passwordEncoder.encode(password));
    }

    @Transactional(rollbackFor = IllegalArgumentException.class)
    @CacheEvict(allEntries = true)
    public void deleteById(Integer id) {
        userRepository.deleteById(id);
    }

    public long count() {
        return userRepository.count();
    }

    @CacheEvict(allEntries = true)
    @Transactional
    public User save(User user) {
        return userRepository.save(user);
    }

    @Cacheable(key = "#root.targetClass.simpleName + #root.methodName + #p0", unless = "#result == null")
    public boolean existsByUserName(String name) {
        return userRepository.existsByUserName(name);
    }

    @CacheEvict(allEntries = true)
    public void resetAuthority() {
        SecurityContext context = SecurityContextHolder.getContext();
        if (context == null || context.getAuthentication() == null || context.getAuthentication().getPrincipal() == null) {
            return;
        }
        if (!(context.getAuthentication().getPrincipal() instanceof UserPrincipal)) {
            return;
        }
        UserPrincipal userPrincipal = (UserPrincipal) context.getAuthentication().getPrincipal();

        List<Item> items = itemRepository.findAllByAuthorId(userPrincipal.getId());
        if (items == null) {
            return;
        }
        List<SimpleGrantedAuthority> authority = itemMapper.toListSimpleGrantedAuthority(items);

        List<Integer> ids = items.parallelStream().map(Item::getId).collect(Collectors.toList());

        String password = userRepository.findPasswordById(userPrincipal.getId());
        if (ids == null || ids.isEmpty()) {
            Authentication auth = new UsernamePasswordAuthenticationToken(userPrincipal, password, authority);
            context.setAuthentication(auth);
            return;
        }
        List<Long> node = nodeService.findByItemIds(ids);
        List<SimpleGrantedAuthority> nodes = nodeMapper.toListSimpleGrantedAuthority(node);
        authority.addAll(nodes);
        Authentication auth = new UsernamePasswordAuthenticationToken(userPrincipal, password, authority);
        context.setAuthentication(auth);
    }
}
