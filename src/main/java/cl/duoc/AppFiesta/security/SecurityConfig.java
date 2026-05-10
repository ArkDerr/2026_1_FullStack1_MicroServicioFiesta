package cl.duoc.AppFiesta.security;

// Genera constructor automáticamente para atributos final
import lombok.RequiredArgsConstructor;

// Marca clase como configuración Spring
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// Permite configurar seguridad HTTP
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

// Configuración Stateless
import org.springframework.security.config.http.SessionCreationPolicy;

// Representa la cadena de filtros de seguridad
import org.springframework.security.web.SecurityFilterChain;

// Filtro estándar de autenticación Spring
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// Indica que esta clase contiene configuración Spring
@Configuration

// Genera constructor automáticamente
@RequiredArgsConstructor
public class SecurityConfig {

    // Inyecta automáticamente JwtAuthenticationFilter
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    // Registra configuración de seguridad como Bean
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)

            // Puede lanzar excepción
            throws Exception {

        // Retorna configuración final de seguridad
        return http

                // Desactiva protección CSRF
                .csrf(csrf -> csrf.disable())

                // Configura aplicación Stateless
                .sessionManagement(session ->

                // No crear sesiones HTTP
                session.sessionCreationPolicy(
                        SessionCreationPolicy.STATELESS))

                // Configura reglas de autorización
                .authorizeHttpRequests(auth -> auth

                        // Permite acceso público
                        .requestMatchers("/api/v1/public/**").permitAll()

                        // Cualquier otro endpoint requiere autenticación
                        .anyRequest().authenticated())

                // Agrega JwtAuthenticationFilter antes del filtro estándar
                .addFilterBefore(

                        // Nuestro filtro JWT personalizado
                        jwtAuthenticationFilter,

                        // Antes del filtro default Spring
                        UsernamePasswordAuthenticationFilter.class)

                // Construye configuración final
                .build();
    }
}