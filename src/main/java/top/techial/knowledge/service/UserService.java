package top.techial.knowledge.service;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
        var qUser = QUser.user;
        return jpaQueryFactory.select(qUser.password).where(qUser.id.eq(id))
                .from(qUser)
                .fetchFirst();
    }

    public void resetAuthority() {
        var context = SecurityContextHolder.getContext();
        if (context == null || context.getAuthentication() == null || context.getAuthentication().getPrincipal() == null) {
            return;
        }
        if (!(context.getAuthentication().getPrincipal() instanceof UserPrincipal)) {
            return;
        }
        var userPrincipal = (UserPrincipal) context.getAuthentication().getPrincipal();

        var qItem = QItem.item;
        var items = jpaQueryFactory.select(qItem.id)
                .from(qItem)
                .where(qItem.author.id.eq(userPrincipal.getId()))
                .fetch();

        if (items == null) {
            return;
        }
        var authority = itemMapper.toListSimpleGrantedAuthority(items);

        var password = findPasswordById(userPrincipal.getId());

        if (items.isEmpty()) {
            Authentication auth = new UsernamePasswordAuthenticationToken(userPrincipal, password, authority);
            context.setAuthentication(auth);
            return;
        }
        var node = nodeRepository.findByItemIdIn(items);
        var nodes = nodeMapper.toListSimpleGrantedAuthority(node);
        authority.addAll(nodes);
        var auth = new UsernamePasswordAuthenticationToken(userPrincipal, password, authority);
        context.setAuthentication(auth);
    }

    public void deleteById(Integer id) {
        itemService.deleteByUserId(id);
        recordRepository.deleteByUserId(id);
        userRepository.deleteById(id);
    }

    @Transactional
    public User updatePassword(Integer id, String srcPassword, String password) {
        var user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);

        if (!passwordEncoder.matches(srcPassword, user.getPassword())) {
            throw new PasswordNotMatchException();
        }

        var qUser = QUser.user;
        jpaQueryFactory.update(qUser).set(qUser.password, passwordEncoder.encode(password))
                .where(qUser.id.eq(id)).execute();
        return user;
    }

    public User update(Integer id, UserVM userVM) {
        return userRepository.findById(id).map(user -> {
            var vmOptional = Optional.of(userVM);
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
