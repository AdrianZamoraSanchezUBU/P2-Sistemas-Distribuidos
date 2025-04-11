package ubu.adrian.practica2.controller;


import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.ui.Model;

/**
 * Controlador de la página de excepciones
 */
@Controller
public class ExceptionController {

	/**
	 * Gestiona las solicitudes de la ruta /file-open-exception
	 * 
	 * @return pagina de excepciones
	 */
	@GetMapping("/file-open-exception")
    public String home(Model model) {
		String flaskApiUrl = "http://flask-api:5000/api/file-not-found";

        RestTemplate restTemplate = new RestTemplate();
        
        try {
            ResponseEntity<Map> response = restTemplate.getForEntity(flaskApiUrl, Map.class);
            String exceptionMessage = (String) response.getBody().get("message");
            model.addAttribute("exceptionMessage", exceptionMessage);
        } catch (Exception e) {
            model.addAttribute("exceptionMessage", "Sin mensaje de excepción.");
        }

        return "exceptions";
    }
}