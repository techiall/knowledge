package top.techial.knowledge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import top.techial.knowledge.config.StorageProperties;

/**
 * @author techial
 */
@SpringBootApplication
@EnableConfigurationProperties({
        StorageProperties.class,
        RedisProperties.class
})
public class KnowledgeApplication {

    public static void main(String[] args) {
        SpringApplication.run(KnowledgeApplication.class, args);
    }

}
