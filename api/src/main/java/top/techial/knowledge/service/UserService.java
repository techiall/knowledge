package top.techial.knowledge.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
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
    private final PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> findByUserName(String userName) {
        return userRepository.findFirstByUserName(userName);
    }

    public Optional<User> findById(String id) {
        return userRepository.findById(id);
    }

    @CacheEvict(allEntries = true)
    public User updatePassword(String id, String password) {
        if (password == null) {
            throw new IllegalArgumentException();
        }
        return userRepository.save(userRepository.findById(id).orElseThrow(NullPointerException::new)
            .setPassword(passwordEncoder.encode(password)));
    }

    @Transactional(rollbackFor = IllegalArgumentException.class)
    @CacheEvict(allEntries = true)
    public void deleteById(String id) {
        userRepository.deleteById(id);
    }

    public long count() {
        return userRepository.count();
    }

    public User save(User user) {
        return userRepository.save(user);
    }
}
