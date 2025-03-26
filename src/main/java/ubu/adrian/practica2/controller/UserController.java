package ubu.adrian.practica2.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controlador de las páginas relacionadas
 * con la gestión de usuarios
 */
@Controller
public class UserController {

	/**
	 * Gestiona las solicitudes de la ruta /login
	 * 
	 * @return pagina de login
	 */
    @GetMapping("/login")
    public String login() {
    	// TODO logica de login
        return "login";
    }
    
    /**
	 * Gestiona las solicitudes de la ruta /user
	 * 
	 * @return panel del usuario
	 */
    @GetMapping("/register")
    public String register() {
    	// TODO logica de registro
        return "user";
    }
    
    /**
	 * Gestiona las solicitudes de la ruta /user
	 * 
	 * @return panel del usuario
	 */
    @GetMapping("/user")
    public String admin() {
        return "user";
    }
}