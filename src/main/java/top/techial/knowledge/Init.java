package top.techial.knowledge;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import top.techial.knowledge.domain.User;
import top.techial.knowledge.repository.UserRepository;

@Component
public class Init implements CommandLineRunner {
    private final UserRepository userRepository;

    public Init(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) {
        userRepository.deleteAll();
        if (userRepository.count() == 0L) {
            userRepository.save(new User().setAccountNonExpired(true).setAccountNonLocked(true).setCredentialsNonExpired(true).setEnabled(true).setNickName("root").setUserName("root").setPassword("{noop}root"));
            userRepository.save(new User().setAccountNonExpired(true).setAccountNonLocked(true).setCredentialsNonExpired(true).setEnabled(true).setNickName("admin").setUserName("admin").setPassword("{noop}admin"));
            userRepository.save(new User().setAccountNonExpired(true).setAccountNonLocked(true).setCredentialsNonExpired(true).setEnabled(true).setNickName("techial").setUserName("techial").setPassword("{noop}techial"));
        }
    }
}
