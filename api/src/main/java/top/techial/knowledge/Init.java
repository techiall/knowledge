package top.techial.knowledge;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import top.techial.knowledge.domain.User;
import top.techial.knowledge.service.UserService;

@Component
public class Init implements CommandLineRunner {
    private final UserService userService;

    public Init(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void run(String... args) {
        userService.deleteAll();
        if (userService.count() == 0L) {
            userService.save(new User().setNickName("root").setUserName("root").setPassword("{noop}root"));
            userService.save(new User().setNickName("admin").setUserName("admin").setPassword("{noop}admin"));
            userService.save(new User().setNickName("techial").setUserName("techial").setPassword("{noop}techial"));
        }

    }
}
