package top.techial.knowledge.config;

import org.hibernate.cache.jcache.ConfigSettings;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.jcache.configuration.RedissonConfiguration;
import org.redisson.spring.session.config.EnableRedissonHttpSession;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.cache.configuration.MutableConfiguration;
import javax.cache.expiry.CreatedExpiryPolicy;
import javax.cache.expiry.Duration;
import java.util.concurrent.TimeUnit;

/**
 * @author techial
 */

@Configuration
@EnableCaching
@EnableRedissonHttpSession
public class CacheConfiguration {
    private final RedisProperties redisProperties;

    public CacheConfiguration(RedisProperties redisProperties) {
        this.redisProperties = redisProperties;
    }

    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        config.useSingleServer().setAddress(redisProperties.getHost());
        config.useSingleServer().setDatabase(redisProperties.getDatabase());
        return Redisson.create(config);
    }

    @Bean
    public javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration() {
        MutableConfiguration<Object, Object> jcacheConfig = new MutableConfiguration<>();
        jcacheConfig.setStatisticsEnabled(true);
        jcacheConfig.setExpiryPolicyFactory(CreatedExpiryPolicy.factoryOf(new Duration(TimeUnit.SECONDS, 3600)));
        return RedissonConfiguration.fromInstance(redissonClient(), jcacheConfig);
    }


    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(javax.cache.CacheManager cm) {
        return hibernateProperties -> hibernateProperties.put(ConfigSettings.CACHE_MANAGER, cm);
    }

}
