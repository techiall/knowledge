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
import top.techial.knowledge.domain.QItem;
import top.techial.knowledge.domain.QUser;
import top.techial.knowledge.repository.NodeRepository;
import top.techial.knowledge.security.UserPrincipal;
import top.techial.knowledge.service.mapper.ItemMapper;
import top.techial.knowledge.service.mapper.NodeMapper;

import java.util.List;

/**
 * @author techial
 */
@Service
@Log4j2
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final NodeMapper nodeMapper;
    private final ItemMapper itemMapper;
    private final NodeRepository nodeRepository;
    private final JPAQueryFactory jpaQueryFactory;

    public UserService(
            PasswordEncoder passwordEncoder,
            NodeMapper nodeMapper,
            ItemMapper itemMapper,
            NodeRepository nodeRepository,
            JPAQueryFactory jpaQueryFactory
    ) {
        this.passwordEncoder = passwordEncoder;
        this.nodeMapper = nodeMapper;
        this.itemMapper = itemMapper;
        this.nodeRepository = nodeRepository;
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

        QItem qItem = QItem.item;
        List<Integer> items = jpaQueryFactory.select(qItem.id)
                .from(qItem)
                .where(qItem.author.id.eq(userPrincipal.getId()))
                .fetch();

        if (items == null) {
            return;
        }
        List<SimpleGrantedAuthority> authority = itemMapper.toListSimpleGrantedAuthority(items);

        String password = findPasswordById(userPrincipal.getId());

        if (items.isEmpty()) {
            Authentication auth = new UsernamePasswordAuthenticationToken(userPrincipal, password, authority);
            context.setAuthentication(auth);
            return;
        }
        List<Long> node = nodeRepository.findByItemIdIn(items);
        List<SimpleGrantedAuthority> nodes = nodeMapper.toListSimpleGrantedAuthority(node);
        authority.addAll(nodes);
        Authentication auth = new UsernamePasswordAuthenticationToken(userPrincipal, password, authority);
        context.setAuthentication(auth);
    }
}
