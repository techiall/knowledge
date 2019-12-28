package top.techial.knowledge.config;

import lombok.extern.log4j.Log4j2;
import org.ehcache.event.CacheEvent;
import org.ehcache.event.CacheEventListener;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

/**
 * @author techial
 */
@Log4j2
@Configuration
@EnableCaching
public class CacheEventLoggerConfig implements CacheEventListener<Object, Object> {

    @Override
    public void onEvent(CacheEvent<?, ?> event) {
        if (log.isDebugEnabled()) {
            log.debug("Event: [{}]\n" +
                    "Key: [{}]\n" +
                    "old value: [{}]\n" +
                    "new value: [{}]",
                event.getType(), event.getKey(), event.getOldValue(), event.getNewValue());
        }

    }
}
