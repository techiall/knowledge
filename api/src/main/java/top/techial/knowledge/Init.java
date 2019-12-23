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
        if (userService.count() == 0L) {
            userService.save(new User().setUserName("root").setPassword("{noop}root"));
            userService.save(new User().setUserName("admin").setPassword("{noop}admin"));
            userService.save(new User().setUserName("techial").setPassword("{noop}techial"));
        }

    }
}
