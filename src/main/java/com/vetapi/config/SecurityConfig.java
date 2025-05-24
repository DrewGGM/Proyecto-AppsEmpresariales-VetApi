package com.vetapi.config;

import com.vetapi.infrastructure.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CorsConfigurationSource corsConfigurationSource;
    
    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // Deshabilitar CSRF para APIs REST
            .csrf(csrf -> csrf.disable())
            
            // Usar la configuración CORS existente
            .cors(cors -> cors.configurationSource(corsConfigurationSource))
            
            // Configurar autorización de requests
            .authorizeHttpRequests(authz -> authz
                // Permitir acceso libre a endpoints de autenticación
                .requestMatchers("/auth/**").permitAll()
                
                // Permitir acceso libre a endpoints de debug (solo para desarrollo)
                .requestMatchers("/debug/**").permitAll()
                
                // Permitir acceso a documentación API
                .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                
                // Permitir acceso a actuator health check
                .requestMatchers("/actuator/health").permitAll()
                
                // Cualquier otra request requiere autenticación
                .anyRequest().authenticated()
            )
            
            // Configurar manejo de sesiones como stateless (para JWT)
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            
            // Agregar filtro JWT antes del filtro de autenticación por defecto
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            
            // Deshabilitar autenticación básica por defecto
            .httpBasic(httpBasic -> httpBasic.disable())
            
            // Deshabilitar formulario de login por defecto
            .formLogin(form -> form.disable());

        return http.build();
    }
} 