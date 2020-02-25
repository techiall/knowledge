package top.techial.knowledge.security;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import top.techial.knowledge.security.handler.AuthenticationEntryPointImpl;
import top.techial.knowledge.security.handler.LogoutHandler;


/**
 * @author techial
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsServiceImpl userDetailsService;
    private final LogoutHandler logoutHandler;
    private final AuthenticationSuccessHandler authenticationSuccessHandler;
    private final AuthenticationFailureHandler authenticationFailureHandler;
    private final AuthenticationEntryPointImpl authenticationEntryPoint;
    private final PasswordEncoder passwordEncoder;
    private final Environment environment;
    private final PersistentTokenRepository persistentTokenRepository;

    public SecurityConfig(
            @Qualifier("userDetailsServiceImpl") UserDetailsServiceImpl userDetailsService,
            LogoutHandler logoutHandler,
            AuthenticationSuccessHandler authenticationSuccessHandler,
            AuthenticationFailureHandler authenticationFailureHandler,
            AuthenticationEntryPointImpl authenticationEntryPoint,
            PasswordEncoder passwordEncoder,
            Environment environment,
            PersistentTokenRepository persistentTokenRepository) {
        this.userDetailsService = userDetailsService;
        this.logoutHandler = logoutHandler;
        this.authenticationSuccessHandler = authenticationSuccessHandler;
        this.authenticationFailureHandler = authenticationFailureHandler;
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.passwordEncoder = passwordEncoder;
        this.environment = environment;
        this.persistentTokenRepository = persistentTokenRepository;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // @formatter:off
        http
            .formLogin()
            .loginPage("/login")
            .loginProcessingUrl("/api/user/login")
            .permitAll()
            .successHandler(authenticationSuccessHandler)
            .failureHandler(authenticationFailureHandler)
        .and()
            .logout()
            .logoutUrl("/api/user/logout")
            .permitAll()
            .logoutSuccessHandler(logoutHandler)
        .and()
            .exceptionHandling()
            .defaultAuthenticationEntryPointFor(authenticationEntryPoint, new AntPathRequestMatcher("/**"))
        .and()
            .rememberMe()
            .tokenRepository(persistentTokenRepository)
        .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
            .sessionAuthenticationFailureHandler(authenticationFailureHandler)
            .sessionFixation()
            .newSession()
            .maximumSessions(1)
            .sessionRegistry(sessionRegistry())
            .maxSessionsPreventsLogin(false);
        accepts(http);
        // @formatter:on
    }


    private void accepts(HttpSecurity httpSecurity) throws Exception {
        if (environment.acceptsProfiles(Profiles.of("dev"))) {
            httpSecurity.authorizeRequests().antMatchers("/api/**").permitAll();
            httpSecurity.csrf().disable();
        } else {
            httpSecurity.authorizeRequests()
                    .antMatchers("/api/user/me", "/api/register/**").permitAll()
                    .antMatchers(
                            HttpMethod.GET,
                            "/api/storage/text/**",
                            "/api/storage/preview/**",
                            "/api/node/**",
                            "/api/search/**"
                    ).permitAll()
                    .antMatchers("/api/**").authenticated();
            httpSecurity.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
        }

    }

    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

}

