package ubu.adrian.practica2.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

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
    public String home() {
        return "home";
    }
}