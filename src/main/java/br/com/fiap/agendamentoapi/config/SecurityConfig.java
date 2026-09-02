package br.com.fiap.agendamentoapi.config;

import br.com.fiap.agendamentoapi.config.security.SecurityFilter;
import br.com.fiap.agendamentoapi.exceptions.handler.GlobalExceptionHandler;
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

    private static final String[] PERFIS_NAO_PACIENTE = {
            "ADMINISTRADOR",
            "MEDICO",
            "ENFERMEIRO",
            "RECEPCIONISTA"
    };

    private static final String[] GESTAO_PESSOAL = {
            "/v1/medico",
            "/v1/medico/**",
            "/v1/enfermeiro",
            "/v1/enfermeiro/**",
            "/v1/recepcionista",
            "/v1/recepcionista/**"
    };

    private static final String[] PACIENTE_CADASTRO = {
            "/v1/paciente",
            "/v1/paciente/**"
    };

    private static final String[] HISTORICO_PACIENTE = {
            "/v1/historico-paciente",
            "/v1/historico-paciente/**"
    };

    private static final String[] AGENDAMENTO = {
            "/v1/agendamento",
            "/v1/agendamento/**"
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
                        .requestMatchers(HttpMethod.GET, GESTAO_PESSOAL).hasAnyRole(PERFIS_NAO_PACIENTE)
                        .requestMatchers(GESTAO_PESSOAL).hasRole("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.POST, "/v1/paciente").hasRole("RECEPCIONISTA")
                        .requestMatchers(HttpMethod.DELETE, PACIENTE_CADASTRO).hasRole("ADMINISTRADOR")
                        .requestMatchers(PACIENTE_CADASTRO).hasAnyRole(PERFIS_NAO_PACIENTE)
                        .requestMatchers(HttpMethod.POST, HISTORICO_PACIENTE).hasAnyRole("MEDICO", "RECEPCIONISTA")
                        .requestMatchers(HttpMethod.PATCH, HISTORICO_PACIENTE).hasAnyRole("MEDICO", "RECEPCIONISTA")
                        .requestMatchers(HttpMethod.GET, HISTORICO_PACIENTE).hasAnyRole("MEDICO", "RECEPCIONISTA", "ENFERMEIRO")
                        .requestMatchers(HttpMethod.DELETE, HISTORICO_PACIENTE).hasRole("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.POST, AGENDAMENTO).hasAnyRole("MEDICO", "RECEPCIONISTA", "ENFERMEIRO")
                        .requestMatchers(HttpMethod.PATCH, AGENDAMENTO).hasRole("RECEPCIONISTA")
                        .requestMatchers(HttpMethod.DELETE, AGENDAMENTO).hasRole("RECEPCIONISTA")
                        .requestMatchers(HttpMethod.GET, AGENDAMENTO).hasAnyRole("MEDICO", "RECEPCIONISTA", "ENFERMEIRO", "PACIENTE")
                        .anyRequest().authenticated())
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint(globalExceptionHandler)
                        .accessDeniedHandler(globalExceptionHandler))
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}