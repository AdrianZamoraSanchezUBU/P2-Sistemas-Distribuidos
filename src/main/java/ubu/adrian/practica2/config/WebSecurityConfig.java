package ubu.adrian.practica2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import ubu.adrian.practica2.repository.UserRepository;
import ubu.adrian.practica2.model.User;

/**
 * Configuración de seguridad para la aplicación
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    private final UserRepository userRepository;

    /**
     * Constructor del repositorio de usuarios
     * 
     * @param userRepository Repositorio para la gestión de usuarios
     */
    public WebSecurityConfig(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Configura la seguridad de la aplicación, incluyendo rutas públicas y autenticación
     * 
     * @param http objeto HttpSecurity para configurar la seguridad
     * @return La cadena de filtros de seguridad configurada
     * @throws Exception si ocurre un error en la configuración
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
        	.authorizeHttpRequests((requests) -> requests
        		// TODO Quitar del permit all las que requieren autenticación al acabar las pruebas
    			.requestMatchers("/login/**", "/register/**", "/admin/**", "/user/**", "/", "/createUser").permitAll()
    			// Ruta de administración
    			.requestMatchers("/admin/**").hasRole("ADMIN")
    			// Restro de requests
                .anyRequest().authenticated()
            )
            .formLogin((form) -> form
            	// Pagina deinicio de sesion
                .loginPage("/login")
                .permitAll()
            )
            .logout((logout) -> logout.permitAll()); // Permite el cierre de sesión sin restricciones

        return http.build();
    }

    /**
     * Busca y devuelve los datos de un usuario a partir de su nombre
     * 
     * @return Un servicio de detalles de usuario
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
        	// Busca el usuario por el nombre de usuario
        	User usuario = userRepository.findByUsername(username);
            
        	// Lanza una excepción si el usuario no existe
            if (usuario == null) {
                throw new UsernameNotFoundException("El usuario: " + username + " no existe");
            }
            
            return usuario;
        };
    }

    /**
     * Codificador de contraseñas utilizando BCrypt
     * 
     * @return Codificador de contraseñas
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}