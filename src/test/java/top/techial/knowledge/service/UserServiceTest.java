package top.techial.knowledge.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import top.techial.knowledge.dao.UserRepository;

@SpringBootTest
@RunWith(SpringRunner.class)
public class UserServiceTest {
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void findByPassword() {
        System.out.println(userRepository.findPasswordById(2));
    }

    @Test
    public void updatePasswordTest() {
        userService.updatePassword(2, "techial");
    }

}
