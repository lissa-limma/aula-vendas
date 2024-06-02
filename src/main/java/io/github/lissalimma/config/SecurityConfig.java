package io.github.lissalimma.config;

import io.github.lissalimma.security.jwt.JwtAuthFilter;
import io.github.lissalimma.security.jwt.JwtService;
import io.github.lissalimma.service.impl.UsuarioServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Autowired
    private UsuarioServiceImpl usuarioService;

    @Autowired
    private JwtService jwtService;

    @Bean
    public OncePerRequestFilter jwtFilter() {
        return new JwtAuthFilter(jwtService, usuarioService);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.userDetailsService(usuarioService);
        http.csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable).authorizeHttpRequests((requests) ->
                {
                    requests.requestMatchers("/", "/home").permitAll();
                    requests.requestMatchers(HttpMethod.POST, "/api/usuarios/**").permitAll();

                    requests.requestMatchers("/api/clientes/**")
                            .hasAnyRole("USER", "ADMIN")
                                .requestMatchers("/api/produtos/**")
                                    .hasAnyRole("ADMIN")
                                        .requestMatchers("/api/pedidos/**")
                                            .hasAnyRole("USER", "ADMIN")
                                                .anyRequest().authenticated();

                }
                ) .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class)
                .httpBasic(Customizer.withDefaults())
                .logout((logout) -> logout.permitAll());

        return http.build();
    }
}
