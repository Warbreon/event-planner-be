package com.cognizant.EventPlanner.config;

import com.cognizant.EventPlanner.model.Role;
import com.cognizant.EventPlanner.security.jwt.AccessDenied;
import com.cognizant.EventPlanner.security.jwt.JwtAuthenticationEntryPoint;
import com.cognizant.EventPlanner.security.jwt.JwtRequestFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityFilterChainConfig {

    private final CorsConfigurationSource corsConfigurationSource;
    private final JwtRequestFilter jwtRequestFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final AccessDenied accessDenied;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors((cors) -> cors.configurationSource(corsConfigurationSource))
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests((authenticationRequest) ->
                        authenticationRequest.requestMatchers("/api/auth/authenticate", "/api/auth/refresh").permitAll())
                .authorizeHttpRequests((eventRequest) -> {
                    eventRequest.requestMatchers(HttpMethod.GET, "/api/events/**")
                            .hasAnyAuthority(Role.USER.name(), Role.EVENT_ADMIN.name(), Role.SYSTEM_ADMIN.name());
                    eventRequest.requestMatchers(HttpMethod.POST,"/api/events/**")
                            .hasAnyAuthority(Role.EVENT_ADMIN.name(),  Role.SYSTEM_ADMIN.name());
                    eventRequest.requestMatchers(HttpMethod.PUT,"/api/events/**")
                            .hasAnyAuthority(Role.EVENT_ADMIN.name(),  Role.SYSTEM_ADMIN.name());
                    eventRequest.requestMatchers(HttpMethod.DELETE,"/api/events/**")
                            .hasAnyAuthority(Role.EVENT_ADMIN.name(),  Role.SYSTEM_ADMIN.name());
                })
                .authorizeHttpRequests((attendeeRequest) -> {
                    attendeeRequest.requestMatchers(HttpMethod.GET, "/api/attendee/**")
                            .hasAnyAuthority(Role.USER.name(), Role.EVENT_ADMIN.name(), Role.SYSTEM_ADMIN.name());
                    attendeeRequest.requestMatchers(HttpMethod.POST, "/api/attendee/**")
                            .hasAnyAuthority(Role.USER.name(), Role.EVENT_ADMIN.name(), Role.SYSTEM_ADMIN.name());
                    attendeeRequest.requestMatchers(HttpMethod.PUT, "/api/attendee/**")
                            .hasAnyAuthority(Role.USER.name(), Role.EVENT_ADMIN.name(), Role.SYSTEM_ADMIN.name());
                    attendeeRequest.requestMatchers(HttpMethod.DELETE, "/api/attendee/**")
                            .hasAnyAuthority(Role.USER.name(), Role.EVENT_ADMIN.name(), Role.SYSTEM_ADMIN.name());
                })
                .exceptionHandling((exceptions) -> {
                    exceptions.authenticationEntryPoint(jwtAuthenticationEntryPoint);
                    exceptions.accessDeniedHandler(accessDenied);
                });

        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
