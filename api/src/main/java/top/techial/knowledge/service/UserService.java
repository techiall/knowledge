package top.techial.knowledge.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.techial.knowledge.dao.UserRepository;
import top.techial.knowledge.domain.User;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Root;
import java.time.Instant;
import java.util.Optional;

/**
 * @author techial
 */
@Service
@Log4j2
@CacheConfig(cacheNames = {"user", "record", "node-relation", "knowledge-node"})
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EntityManager entityManager;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, EntityManager entityManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.entityManager = entityManager;
    }

    public Optional<User> findByUserName(String userName) {
        return userRepository.findFirstByUserName(userName);
    }

    public Optional<User> findById(Integer id) {
        return userRepository.findById(id);
    }

    @CacheEvict(allEntries = true)
    @Transactional
    public void updatePassword(Integer id, String password) {
        if (password == null) {
            throw new IllegalArgumentException();
        }
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaUpdate<User> criteria = builder.createCriteriaUpdate(User.class);
        Root<User> root = criteria.from(User.class);
        criteria.set(root.get("password"), passwordEncoder.encode(password));
        criteria.set(root.get("updateTime"), Instant.now());
        criteria.where(builder.equal(root.get("id"), id));
        entityManager.createQuery(criteria).executeUpdate();
    }

    @Transactional(rollbackFor = IllegalArgumentException.class)
    @CacheEvict(allEntries = true)
    public void deleteById(Integer id) {
        userRepository.deleteById(id);
    }

    @Cacheable(key = "#root.targetClass.simpleName + #root.methodName", unless = "#result == null")
    public long count() {
        return userRepository.count();
    }

    @CacheEvict(allEntries = true)
    @Transactional
    public User save(User user) {
        return userRepository.save(user);
    }

    @Cacheable(key = "#root.targetClass.simpleName + #root.methodName + p0", unless = "#result == null")
    public boolean existsByUserName(String name) {
        return userRepository.existsByUserName(name);
    }

}
