package ubu.adrian.practica2.controller;


import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;

/**
 * Controlador de la página principal
 */
@Controller
public class HomeController {

	/**
	 * Gestiona las solicitudes de la ruta /
	 * 
	 * @return pagina home
	 */
	@GetMapping("/")
    public String home(Model model, Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            model.addAttribute("username", authentication.getName());
        }
        return "home";
    }
}