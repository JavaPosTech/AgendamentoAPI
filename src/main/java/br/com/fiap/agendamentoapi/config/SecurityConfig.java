package br.com.fiap.agendamentoapi.config;

import br.com.fiap.agendamentoapi.exceptions.handler.GlobalExceptionHandler;
import br.com.fiap.agendamentoapi.config.security.SecurityFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final SecurityFilter securityFilter;

    private static final String[] ENDPOINTS_PUBLICOS = {
            "/v1/auth/login",
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html"
    };

    private static final String[] ENDPOINTS_CADASTRO_PUBLICO = {
            "/v1/medico",
            "/v1/paciente",
            "/v1/enfermeiro"
    };

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity pHttpSecurity, GlobalExceptionHandler globalExceptionHandler) {
        return pHttpSecurity.csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers(ENDPOINTS_PUBLICOS).permitAll()
                        .requestMatchers(HttpMethod.POST, ENDPOINTS_CADASTRO_PUBLICO).permitAll()
                        .anyRequest().authenticated())
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint(globalExceptionHandler)
                        .accessDeniedHandler(globalExceptionHandler))
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}