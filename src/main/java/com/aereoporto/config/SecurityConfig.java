package com.aereoporto.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.core.userdetails.UserDetailsService;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private DataSource dataSource;

    /**
     * The main security filter chain, which defines the security configuration
     * for all incoming requests.
     *
     * <p>
     * This method configures the following security rules:
     * <ul>
     * <li>Allow all requests to the root page, static resources, and the login
     * page, search pages, and book page.
     * <li>Allow all requests to the "/admin/**" path if the user has the
     * "ADMIN" role.
     * <li>Require all other requests to be authenticated.
     * </ul>
     *
     * @param http the {@link HttpSecurity} object to configure
     * @return the configured {@link SecurityFilterChain}
     * @throws Exception if any error occurs while configuring the filter chain
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/img/**", "/css/**", "/js/**",
                                "/admin/login-page", "/results/**",
                                "/search-flights**", "/book")
                        .permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated())
                .formLogin(form -> form
                        .loginPage("/admin/login-page")
                        .loginProcessingUrl("/admin/login")
                        .defaultSuccessUrl("/admin/dashboard", true)
                        .failureUrl("/admin/login-page?error=true")
                        .permitAll())
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/admin/login")
                        .permitAll());

        return http.build();
    }

    /**
     * Configures a {@link UserDetailsService} to be used by the {@link
     * org.springframework.security.authentication.ProviderManager} to authenticate
     * users.
     * <p>
     * This service uses a {@link JdbcUserDetailsManager} to load user details from
     * the database. It uses two queries:
     * <ol>
     * <li>A query to load the user's username, password, and enabled status from
     * the table "AMMINISTRATORE".
     * <li>A query to load the user's roles from the table "AMMINISTRATORE".
     * </ol>
     * <p>
     * The queries are configured using the {@link
     * org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl} and the
     * {@link org.springframework.security.core.userdetails.jdbc.JdbcUserDetailsManager}
     * APIs.
     */
    @Bean
    public UserDetailsService userDetailsService() {
        JdbcUserDetailsManager manager = new JdbcUserDetailsManager(dataSource);
        manager.setUsersByUsernameQuery(
                "SELECT username, password, true as enabled FROM AMMINISTRATORE WHERE username = ?");
        manager.setAuthoritiesByUsernameQuery(
                "SELECT username, role FROM AMMINISTRATORE WHERE username = ?");
        return manager;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
