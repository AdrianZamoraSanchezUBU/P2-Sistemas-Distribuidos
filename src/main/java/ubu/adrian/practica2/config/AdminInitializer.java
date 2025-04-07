package ubu.adrian.practica2.config;

import ubu.adrian.practica2.model.User;
import ubu.adrian.practica2.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

@Component
public class AdminInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        
    	User admin = userRepository.findByUsername("admin");
    	
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
