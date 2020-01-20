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

import java.util.Optional;

/**
 * @author techial
 */
@Service
@Log4j2
@CacheConfig(cacheNames = "user")
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
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

    @Cacheable(key = "#root.targetClass.simpleName + #root.methodName", unless = "#result == null")
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

}
