package top.techial.knowledge;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;
import top.techial.knowledge.domain.User;
import top.techial.knowledge.repository.UserRepository;

import java.io.IOException;
import java.util.Arrays;

@Component
public class Init implements CommandLineRunner {
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;

    public Init(UserRepository userRepository, ObjectMapper objectMapper) {
        this.userRepository = userRepository;
        this.objectMapper = objectMapper;
    }

    private static byte[] resource(String path) throws IOException {
        ClassPathResource classPathResource = new ClassPathResource(path);
        return FileCopyUtils.copyToByteArray(classPathResource.getInputStream());
    }

    @Override
    public void run(String... args) throws IOException {
        if (userRepository.count() == 0L) {
            byte[] bytes = resource("data/user.json");
            User[] users = objectMapper.readValue(bytes, User[].class);
            userRepository.saveAll(Arrays.asList(users));
        }
    }
}
