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
import org.springframework.web.client.RestTemplate;

import ubu.adrian.practica2.repository.UserRepository;
import ubu.adrian.practica2.model.User;

/**
 * Configuración de seguridad para la aplicación
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
	// Referencia al repositorio de usuario
    private final UserRepository userRepository;

    /**
     * Constructor de la configuración de seguridad
     * 
     * @param userRepository Repositorio para la gestión de usuarios
     */
    public WebSecurityConfig(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Configura la seguridad de la aplicación, incluyendo rutas públicas y autenticación
     * 
     * @param http Objeto HttpSecurity para configurar la seguridad
     * @return La cadena de filtros de seguridad configurada
     * @throws Exception si ocurre un error en la configuración
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
        	.authorizeHttpRequests((requests) -> requests
        		// Paginas a las que se puede acceder sin login
    			.requestMatchers("/", "/register", "/login/**", "/create-user", "/exception-menu", "/file/**", "/db/**", "/pokemon/**").permitAll()
    			// Ruta de administración
    			.requestMatchers("/user-list/**", "/remove", "/update-user-data").hasRole("ADMIN")
    			// Restro de requests autenticadas por defecto
                .anyRequest().authenticated()
            )
            .formLogin((form) -> form
            	// Pagina deinicio de sesion
                .loginPage("/login") // URL de la página de login
                .usernameParameter("username")
                .passwordParameter("password")
                .defaultSuccessUrl("/", true) // Redirige al home al iniciar sesión
                .permitAll()
            )
            .logout((logout) -> logout.permitAll()
        		.logoutUrl("/logout")  // URL para activar el logout
                .logoutSuccessUrl("/?logout")
                .invalidateHttpSession(true) // Invalida la sesión
                .deleteCookies("JSESSIONID") // Borrar las cookies
                .permitAll()
    		);

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
    
    /**
     * Plantilla del modelo REST
     * 
     * @return Objeto de plantilla REST
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}