package top.techial.knowledge;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;
import top.techial.knowledge.domain.User;
import top.techial.knowledge.repository.UserRepository;
import top.techial.knowledge.utils.JsonUtils;

import java.io.IOException;
import java.util.Arrays;

@Component
public class Init implements CommandLineRunner {
    private final UserRepository userRepository;

    public Init(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private static byte[] resource(String path) throws IOException {
        final ClassPathResource classPathResource = new ClassPathResource(path);
        return FileCopyUtils.copyToByteArray(classPathResource.getInputStream());
    }

    @Override
    public void run(String... args) throws IOException {
        if (userRepository.count() == 0L) {
            final byte[] bytes = resource("data/user.json");
            final var users = JsonUtils.DEFAULT_MAPPER.readValue(bytes, User[].class);
            userRepository.saveAll(Arrays.asList(users));
        }
    }
}
