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
import top.techial.knowledge.domain.QItem;
import top.techial.knowledge.domain.QUser;
import top.techial.knowledge.domain.User;
import top.techial.knowledge.repository.NodeRepository;
import top.techial.knowledge.repository.RecordRepository;
import top.techial.knowledge.repository.UserRepository;
import top.techial.knowledge.security.UserPrincipal;
import top.techial.knowledge.service.mapper.ItemMapper;
import top.techial.knowledge.service.mapper.NodeMapper;
import top.techial.knowledge.web.rest.errors.PasswordNotMatchException;
import top.techial.knowledge.web.rest.errors.UserNotFoundException;
import top.techial.knowledge.web.rest.errors.UsernameIsRegisterException;
import top.techial.knowledge.web.rest.vm.UserVM;

import java.util.List;
import java.util.Optional;

/**
 * @author techial
 */
@Service
@Log4j2
public class UserService {

    private final UserRepository userRepository;
    private final ItemService itemService;
    private final RecordRepository recordRepository;
    private final PasswordEncoder passwordEncoder;
    private final NodeMapper nodeMapper;
    private final ItemMapper itemMapper;
    private final NodeRepository nodeRepository;
    private final JPAQueryFactory jpaQueryFactory;

    public UserService(
            UserRepository userRepository,
            ItemService itemService,
            RecordRepository recordRepository,
            PasswordEncoder passwordEncoder,
            NodeMapper nodeMapper,
            ItemMapper itemMapper,
            NodeRepository nodeRepository,
            JPAQueryFactory jpaQueryFactory
    ) {
        this.userRepository = userRepository;
        this.itemService = itemService;
        this.recordRepository = recordRepository;
        this.passwordEncoder = passwordEncoder;
        this.nodeMapper = nodeMapper;
        this.itemMapper = itemMapper;
        this.nodeRepository = nodeRepository;
        this.jpaQueryFactory = jpaQueryFactory;
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

    public void deleteById(Integer id) {
        itemService.deleteByUserId(id);
        recordRepository.deleteByUserId(id);
        userRepository.deleteById(id);
    }

    public User updatePassword(Integer id, String srcPassword, String password) {
        User user = userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);

        if (!passwordEncoder.matches(srcPassword, user.getPassword())) {
            throw new PasswordNotMatchException();
        }

        QUser qUser = QUser.user;
        jpaQueryFactory.update(qUser)
                .set(qUser.password, passwordEncoder.encode(password))
                .where(qUser.id.eq(id))
                .execute();
        return user;
    }

    public User update(Integer id, UserVM userVM) {
        return userRepository.findById(id).map(user -> {
            Optional<UserVM> vmOptional = Optional.of(userVM);
            vmOptional.map(UserVM::getImage).filter(it -> !it.isEmpty()).ifPresent(user::setImages);
            vmOptional.map(UserVM::getNickName).filter(it -> !it.isEmpty()).ifPresent(user::setNickName);
            return userRepository.save(user);
        }).orElseThrow(UserNotFoundException::new);
    }

    public User saveNewUser(User user) {
        if (userRepository.existsByUserName(user.getUserName())) {
            throw new UsernameIsRegisterException();
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }
}
