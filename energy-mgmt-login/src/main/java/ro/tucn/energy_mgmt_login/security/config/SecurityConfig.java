package ro.tucn.energy_mgmt_login.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.web.cors.CorsConfigurationSource;
import ro.tucn.energy_mgmt_login.exception.AccessDeniedHandlerBean;
import ro.tucn.energy_mgmt_login.repository.UserRepository;
import ro.tucn.energy_mgmt_login.security.filter.LoginFilter;
import ro.tucn.energy_mgmt_login.security.filter.AuthorizationFilter;
import ro.tucn.energy_mgmt_login.security.service.UserDetailsServiceBean;
import ro.tucn.energy_mgmt_login.security.util.SecurityConstants;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(
            HttpSecurity http,
            LoginFilter loginFilter,
            AuthorizationFilter authorizationFilter,
            AccessDeniedHandler accessDeniedHandler,
            CorsConfigurationSource corsConfigurationSource
    ) throws Exception {
        return http
                .cors(cors -> cors.configurationSource(corsConfigurationSource))
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(handler -> handler.accessDeniedHandler(accessDeniedHandler))
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers(SecurityConstants.AUTH_PATHS_TO_SKIP).permitAll()
                        .requestMatchers(SecurityConstants.SWAGGER_PATHS_TO_SKIP).permitAll()
                        .requestMatchers(SecurityConstants.REGISTER_URL).permitAll()
                        .anyRequest().authenticated())
                .addFilter(loginFilter)
                .addFilterAfter(authorizationFilter, LoginFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            HttpSecurity http,
            PasswordEncoder passwordEncoder,
            UserDetailsService userDetailsService
    ) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);

        return authenticationManagerBuilder.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(SecurityConstants.PASSWORD_STRENGTH);
    }

    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository) {
        return new UserDetailsServiceBean(userRepository);
    }

    @Bean
    public AuthorizationFilter authorizationManager(ObjectMapper objectMapper) {
        return new AuthorizationFilter(objectMapper);
    }

    @Bean
    public LoginFilter loginFilter(ObjectMapper objectMapper, AuthenticationManager authenticationManager) {
        return new LoginFilter(objectMapper, authenticationManager);
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler(ObjectMapper objectMapper) {
        return new AccessDeniedHandlerBean(objectMapper);
    }
}