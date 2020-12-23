package cc.techial.knowledge.service;

import cc.techial.knowledge.domain.QItem;
import cc.techial.knowledge.domain.QUser;
import cc.techial.knowledge.domain.User;
import cc.techial.knowledge.repository.NodeRepository;
import cc.techial.knowledge.repository.RecordRepository;
import cc.techial.knowledge.repository.UserRepository;
import cc.techial.knowledge.security.UserPrincipal;
import cc.techial.knowledge.service.mapper.ItemMapper;
import cc.techial.knowledge.service.mapper.NodeMapper;
import cc.techial.knowledge.web.rest.errors.PasswordNotMatchException;
import cc.techial.knowledge.web.rest.errors.UserNotFoundException;
import cc.techial.knowledge.web.rest.errors.UsernameIsRegisterException;
import cc.techial.knowledge.web.rest.vm.UserVM;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * @author techial
 */
@Service
public class UserService {

    private final UserRepository userRepository;
    private final ItemService itemService;
    private final RecordRepository recordRepository;
    private final PasswordEncoder passwordEncoder;
    private final NodeMapper nodeMapper;
    private final ItemMapper itemMapper;
    private final NodeRepository nodeRepository;
    private final JPAQueryFactory jpaQueryFactory;

    public UserService(UserRepository userRepository,
                       ItemService itemService,
                       RecordRepository recordRepository,
                       PasswordEncoder passwordEncoder,
                       NodeMapper nodeMapper,
                       ItemMapper itemMapper,
                       NodeRepository nodeRepository,
                       JPAQueryFactory jpaQueryFactory) {
        this.userRepository = userRepository;
        this.itemService = itemService;
        this.recordRepository = recordRepository;
        this.passwordEncoder = passwordEncoder;
        this.nodeMapper = nodeMapper;
        this.itemMapper = itemMapper;
        this.nodeRepository = nodeRepository;
        this.jpaQueryFactory = jpaQueryFactory;
    }

    private String findPasswordById(int id) {
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

    public void deleteById(int id) {
        itemService.deleteByUserId(id);
        recordRepository.deleteByUserId(id);
        userRepository.deleteById(id);
    }

    @Transactional
    public User updatePassword(int id, String srcPassword, String password) {
        var user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);

        if (!passwordEncoder.matches(srcPassword, user.getPassword())) {
            throw new PasswordNotMatchException();
        }

        var qUser = QUser.user;
        jpaQueryFactory.update(qUser).set(qUser.password, passwordEncoder.encode(password))
                       .where(qUser.id.eq(id)).execute();
        return user;
    }

    @Transactional
    public User update(int id, UserVM userVM) {
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
