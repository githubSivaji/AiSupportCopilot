package com.sivaji.aisupportcopilot.config;

import com.sivaji.aisupportcopilot.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig{
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        http
                // REST API → no server-side CSRF state
                .csrf(csrf -> csrf.disable())

                // JWT → stateless
                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )




                        .authorizeHttpRequests(auth -> auth

                                .requestMatchers("/api/auth/**")
                                .permitAll()

                                .requestMatchers("/api/users")
                                .permitAll()

                                .requestMatchers("/api/admin/**")
                                .hasRole("ADMIN")

                                .requestMatchers("/api/support/**")
                                .hasAnyRole("SUPPORT_AGENT", "ADMIN")

                                // Product management
                                .requestMatchers(HttpMethod.POST, "/api/products")
                                .hasRole("ADMIN")

                                .requestMatchers(HttpMethod.PUT, "/api/products/**")
                                .hasRole("ADMIN")

                                .requestMatchers(HttpMethod.PATCH, "/api/products/**")
                                .hasRole("ADMIN")

                                // Product viewing
                                .requestMatchers(HttpMethod.GET, "/api/products/**")
                                .authenticated()

                                .anyRequest()
                                .authenticated()
                        )

                // H2 console uses frames
                .headers(headers ->
                        headers.frameOptions(frame ->
                                frame.disable()
                        )
                )

                // JWT filter
                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring()
                .requestMatchers(
                        "/h2-console",
                        "/h2-console/**"
                );
    }
}
