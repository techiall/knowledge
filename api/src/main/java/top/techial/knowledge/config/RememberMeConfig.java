package top.techial.knowledge.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;
import java.util.List;

/**
 * @author techial
 */
@Configuration
public class RememberMeConfig {
    private final DataSource dataSource;
    private final JdbcTemplate jdbcTemplate;
    private static final String SQL = "show tables like 'persistent_logins'";

    public RememberMeConfig(DataSource dataSource, JdbcTemplate jdbcTemplate) {
        this.dataSource = dataSource;
        this.jdbcTemplate = jdbcTemplate;
    }

    private boolean tableExists() {
        List<String> result = jdbcTemplate.queryForList(SQL, String.class);
        return result.isEmpty();
    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);
        jdbcTokenRepository.setCreateTableOnStartup(tableExists());
        return jdbcTokenRepository;
    }
}
