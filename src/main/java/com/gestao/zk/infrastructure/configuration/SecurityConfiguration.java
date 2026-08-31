package com.gestao.zk.infrastructure.configuration;

import com.gestao.zk.domain.repository.UsuarioRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    private static final String ROOT = "/";
    private static final String LOGIN = "/login";
    private static final String LOGIN_PAGE_HTML = LOGIN + ".html";

    private static final String FAILURE_URL = LOGIN_PAGE_HTML + "?error";
    private static final String LOGOUT_SUCCESS_URL = LOGIN_PAGE_HTML + "?logout";
    private static final String[] REQUEST_MATCHERS = { LOGIN_PAGE_HTML, "/css/**", "/js/**", "/images/**" };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity security) throws Exception {
        return security.authorizeHttpRequests(this::authorizeHttpRequestsConfigurer)
                .formLogin(this::formLoginConfigurer)
                .logout(this::logoutConfigurer)
                .csrf(AbstractHttpConfigurer::disable)
                .build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (security) -> security
                .httpFirewall(allowUrlEncodedDoubleSlashFirewall());
    }

    @Bean
    public HttpFirewall allowUrlEncodedDoubleSlashFirewall() {
        var firewall = new StrictHttpFirewall();
        firewall.setAllowUrlEncodedDoubleSlash(true);
        return firewall;
    }

    @Bean
    public UserDetailsService userDetailsService(UsuarioRepository usuarioRepository) {
        return username -> usuarioRepository.findByUsername(username)
                .map(usuario -> User.builder()
                        .username(usuario.getUsername())
                        .password(usuario.getPassword())
                        .roles(usuario.getRole())
                        .build())
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    private AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry authorizeHttpRequestsConfigurer(
            AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry configurer
    ) {
        return configurer
                .requestMatchers(REQUEST_MATCHERS)
                .permitAll()
                .anyRequest()
                .authenticated();
    }

    private FormLoginConfigurer<HttpSecurity> formLoginConfigurer(FormLoginConfigurer<HttpSecurity> configurer) {
        return configurer
                .loginPage(LOGIN_PAGE_HTML)
                .loginProcessingUrl(LOGIN)
                .defaultSuccessUrl(ROOT, true)
                .failureUrl(FAILURE_URL)
                .permitAll();
    }

    private LogoutConfigurer<HttpSecurity> logoutConfigurer(LogoutConfigurer<HttpSecurity> configurer) {
        return configurer
                .logoutSuccessUrl(LOGOUT_SUCCESS_URL)
                .permitAll();
    }
}
