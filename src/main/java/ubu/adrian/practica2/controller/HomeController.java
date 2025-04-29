package ubu.adrian.practica2.controller;


import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;

/**
 * Controlador de la página principal
 * 
 * @author Adrián Zamora Sánchez (azs1004@alu.ubu.es)
 */
@Controller
public class HomeController {

	/**
	 * Gestiona las solicitudes de la ruta /
	 * 
	 * @param model Modelo donde se insertan los datos
	 * @param authentication Gestor de autentificaciones
	 * @return página home
	 */
	@GetMapping("/")
    public String home(Model model, Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            model.addAttribute("username", authentication.getName());
        }
        return "home";
    }
	
	/**
	 * Gestiona las solicitudes de la ruta /exception-menu
	 * 
	 * @return página de excepciones
	 */
	@GetMapping("/exception-menu")
    public String menu() {
        return "exceptionMenu";
    }
}