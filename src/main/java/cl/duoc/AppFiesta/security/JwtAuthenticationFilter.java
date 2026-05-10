package cl.duoc.AppFiesta.security;

// Importa clases para manejo de entrada/salida
import java.io.IOException;

// Importa List para crear lista de roles/permisos
import java.util.List;

// Permite leer valores desde application.properties
import org.springframework.beans.factory.annotation.Value;

// Representa autenticación de usuario en Spring Security
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

// Representa roles/permisos simples como ROLE_ADMIN
import org.springframework.security.core.authority.SimpleGrantedAuthority;

// Permite guardar la autenticación del usuario actual
import org.springframework.security.core.context.SecurityContextHolder;

// Marca esta clase como componente administrado por Spring
import org.springframework.stereotype.Component;

// Clase base para filtros que se ejecutan una sola vez por request
import org.springframework.web.filter.OncePerRequestFilter;

// Librería JWT para validar tokens
import com.auth0.jwt.JWT;

// Algoritmo utilizado para verificar firma JWT
import com.auth0.jwt.algorithms.Algorithm;

// Representa el JWT ya decodificado
import com.auth0.jwt.interfaces.DecodedJWT;

// Clases Servlet para filtros HTTP
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;

// Representa request HTTP
import jakarta.servlet.http.HttpServletRequest;

// Representa response HTTP
import jakarta.servlet.http.HttpServletResponse;

// Indica que Spring debe crear automáticamente esta clase
@Component

// Clase filtro que se ejecuta una vez por request
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    // Lee la propiedad jwt.secret desde application.properties
    @Value("${jwt.secret}")

    // Variable donde se guarda la clave secreta
    private String secret;

    // Método principal del filtro
    @Override
    protected void doFilterInternal(

            // Request HTTP entrante
            HttpServletRequest request,

            // Response HTTP saliente
            HttpServletResponse response,

            // Cadena de filtros de Spring Security
            FilterChain filterChain)

            // Excepciones posibles
            throws ServletException, IOException {

        // Obtiene el header Authorization
        String header = request.getHeader("Authorization");

        // Si no existe Authorization o no comienza con Bearer
        if (header == null || !header.startsWith("Bearer ")) {

            // Continúa normalmente al siguiente filtro
            filterChain.doFilter(request, response);

            // Termina ejecución del método
            return;
        }

        try {

            // Extrae el token quitando "Bearer "
            String token = header.substring(7);

            // Valida el JWT utilizando la clave secreta
            DecodedJWT jwt = JWT.require(

                    // Usa algoritmo HMAC256 y la clave secreta
                    Algorithm.HMAC256(secret))

                    // Verifica que el emisor sea login-service
                    .withIssuer("login-service")

                    // Construye el verificador
                    .build()

                    // Verifica el token
                    .verify(token);

            // Obtiene el username desde el payload JWT
            String username = jwt.getSubject();

            // Obtiene el role desde el claim role
            String role = jwt.getClaim("role").asString();

            // Crea objeto Authentication para Spring Security
            UsernamePasswordAuthenticationToken authentication =

                    // Constructor Authentication
                    new UsernamePasswordAuthenticationToken(

                            // Usuario autenticado
                            username,

                            // Password null porque JWT ya autenticó
                            null,

                            // Lista de roles/permisos
                            List.of(

                                    // Agrega ROLE_ADMIN o ROLE_USER
                                    new SimpleGrantedAuthority("ROLE_" + role)));

            // Guarda la autenticación del usuario en Spring Security
            SecurityContextHolder.getContext()

                    // Define usuario autenticado actual
                    .setAuthentication(authentication);

        } catch (Exception e) {

            // Si JWT es inválido responde 401 Unauthorized
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

            // Termina ejecución
            return;
        }

        // Continúa al siguiente filtro o controller
        filterChain.doFilter(request, response);
    }
}