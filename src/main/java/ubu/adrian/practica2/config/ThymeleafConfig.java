package ubu.adrian.practica2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.extras.springsecurity6.dialect.SpringSecurityDialect;

/**
 * Clase que establece el dialecto/formato de seguridad con el cual Thymeleaf se entiende
 * con Spring Boot Security y permite el uso de roles en las plantillas HTML
 */
@Configuration
public class ThymeleafConfig {
    
	/**
	 * Establece un acuerdo sobre el formato de seguridad
	 * 
	 * @return Formato de seguridad
	 */
    @Bean
    public SpringSecurityDialect springSecurityDialect() {
        return new SpringSecurityDialect();
    }
}