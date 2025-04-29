package ubu.adrian.practica2.config;

import ubu.adrian.practica2.model.User;
import ubu.adrian.practica2.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Clase encargada de generar al usuario admin cuando se ejecura la app
 * 
 * Operación idempotente, una vez creado no hace nada, por más veces que se ejecute
 */
@Component
public class AdminInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Ejecuta una comprobación de la existencia de un usuario administrador
     * en caso de no haber uno lo crea. En el caso contrario no hace nada.
     * 
     * @param args Argumentos de ejecución
     */
    @Override
    public void run(String... args) {
        // Obtiene al admin
    	User admin = userRepository.findByUsername("admin");
    	
    	// Comprueba si hay admin
    	if (admin == null) {
    		// Se genera al admin
            admin = new User();
            
            // Datos del admin
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin"));
            admin.setRol("ADMIN");
            
            userRepository.save(admin);
            System.out.println("Usuario \"admin\" operativo");
        }
    }
}
