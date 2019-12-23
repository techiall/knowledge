package top.techial.knowledge.file.storage;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author techial
 */
@Component
@ConfigurationProperties(prefix = "storage")
@Data
public class StorageProperties {

    private String location;

}
