package top.techial.knowledge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import top.techial.knowledge.config.StorageProperties;

/**
 * @author techial
 */
@SpringBootApplication
@EntityScan(basePackages = "top.techial.knowledge.domain")
@EnableConfigurationProperties({
    StorageProperties.class
})
public class KnowledgeApplication {

    public static void main(String[] args) {
        SpringApplication.run(KnowledgeApplication.class, args);
    }

}
