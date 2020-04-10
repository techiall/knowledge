package top.techial.knowledge.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author techial
 */
@Component
@ConfigurationProperties(prefix = "storage")
public class StorageProperties {

    private String location;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
