package ubu.adrian.practica2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Clase principal de la aplicacion web
 * 
 * Inicia los repositorios y modelos del sistema
 */
@SpringBootApplication
@EnableAutoConfiguration
@EnableJpaRepositories("ubu.adrian.practica2.repository")
@EntityScan("ubu.adrian.practica2.model")
public class Practica2Application {
	
	/**
	 * Método principal de la clase, inicia el sistema
	 * 
	 * @param args parámetros de aplicación
	 */
	public static void main(String[] args) {
		SpringApplication.run(Practica2Application.class, args);
	}
}