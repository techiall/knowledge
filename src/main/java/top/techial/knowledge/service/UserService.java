package top.techial.knowledge.service;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.techial.knowledge.domain.Item;
import top.techial.knowledge.domain.QUser;
import top.techial.knowledge.repository.ItemRepository;
import top.techial.knowledge.security.UserPrincipal;
import top.techial.knowledge.service.mapper.ItemMapper;
import top.techial.knowledge.service.mapper.NodeMapper;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author techial
 */
@Service
@Log4j2
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final ItemRepository itemRepository;
    private final NodeService nodeService;
    private final NodeMapper nodeMapper;
    private final ItemMapper itemMapper;
    private final JPAQueryFactory jpaQueryFactory;

    public UserService(
            PasswordEncoder passwordEncoder,
            ItemRepository itemRepository,
            NodeService nodeService,
            NodeMapper nodeMapper,
            ItemMapper itemMapper,
            JPAQueryFactory jpaQueryFactory
    ) {
        this.passwordEncoder = passwordEncoder;
        this.itemRepository = itemRepository;
        this.nodeService = nodeService;
        this.nodeMapper = nodeMapper;
        this.itemMapper = itemMapper;
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Transactional
    public void updatePassword(Integer id, String password) {
        QUser qUser = QUser.user;
        jpaQueryFactory.update(qUser)
                .set(qUser.password, passwordEncoder.encode(password))
                .where(qUser.id.eq(id))
                .execute();
    }

    private String findPasswordById(Integer id) {
        QUser qUser = QUser.user;
        return jpaQueryFactory.select(qUser.password).where(qUser.id.eq(id))
                .from(qUser)
                .fetchFirst();
    }

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

        String password = findPasswordById(userPrincipal.getId());
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
