package top.techial.knowledge.security;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import top.techial.knowledge.security.handler.AuthenticationEntryPointImpl;
import top.techial.knowledge.security.handler.LogoutHandler;

import java.util.concurrent.TimeUnit;


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

    public SecurityConfig(@Qualifier("userDetailsServiceImpl") UserDetailsServiceImpl userDetailsService, LogoutHandler logoutHandler, AuthenticationSuccessHandler authenticationSuccessHandler, AuthenticationFailureHandler authenticationFailureHandler, AuthenticationEntryPointImpl authenticationEntryPoint, PasswordEncoder passwordEncoder) {
        this.userDetailsService = userDetailsService;
        this.logoutHandler = logoutHandler;
        this.authenticationSuccessHandler = authenticationSuccessHandler;
        this.authenticationFailureHandler = authenticationFailureHandler;
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
            .antMatchers("/api/user/me", "/api/register/**", "/api/session/**")
            .permitAll()
            .antMatchers("/api/**", "/actuator/**").authenticated()

            .and()

            .formLogin()
            .loginPage("/login")
            .loginProcessingUrl("/api/user/login")
            .permitAll()
            .usernameParameter("username")
            .passwordParameter("password")
            .successHandler(authenticationSuccessHandler)
            .failureHandler(authenticationFailureHandler)

            .and()

            .logout()
            .logoutUrl("/api/user/logout")
            .permitAll()
            .deleteCookies("remember-me", "JSESSIONID")
            .logoutSuccessHandler(logoutHandler)

            .and()

            .exceptionHandling()
            .defaultAuthenticationEntryPointFor(authenticationEntryPoint, new AntPathRequestMatcher("/**"))

            .and()

            .rememberMe().tokenValiditySeconds(Math.toIntExact(TimeUnit.DAYS.toSeconds(7)))

            .and()

            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
            .sessionAuthenticationFailureHandler(authenticationFailureHandler)
            .sessionFixation()
            .newSession()
            .maximumSessions(1)
            .sessionRegistry(sessionRegistry())
            .maxSessionsPreventsLogin(false);

        http.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(this.userDetailsService).passwordEncoder(passwordEncoder);
    }

    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}

