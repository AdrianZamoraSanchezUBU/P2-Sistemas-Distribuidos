package ubu.adrian.practica2.controller;


import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.ui.Model;

/**
 * Controlador del panel de excepciones con la API de Pokemon
 * 
 * @author Adrián Zamora Sánchez (azs1004@alu.ubu.es)
 */
@Controller
public class PokemonExceptionController {

	@Autowired
    private RestTemplate restTemplate;

	/**
	 * Gestiona las solicitudes de la ruta /pokemon/menu
	 * 
	 * @return pagina de excepciones
	 */
    @GetMapping("/pokemon/menu")
    public String dbMenu(Model model) {
    	return "exceptionsAPI";
    }
    
    /**
	 * Gestiona las solicitudes de la ruta /pokemon/query
	 * 
	 * @return pagina de excepciones
	 */
    @GetMapping("/pokemon/query")
    public String dbConection(@RequestParam String name, Model model) {
    	try {
    		// Llamada a la API para establecer una conexión
            ResponseEntity<Map> response = restTemplate.getForEntity(
            		"http://flask-api:5000/api/pokemon/query-pokemon?name=" + name, Map.class);

        	// Obtener el campo "response"
        	Map<String, Object> responseBody = (Map<String, Object>) response.getBody().get("response");
            
        	int httpStatus = response.getStatusCode().value(); 	
        	
            // Se establecen los atributos pasados al HTML
        	model.addAttribute("name", responseBody.get("name"));
        	model.addAttribute("id", responseBody.get("id"));
        	model.addAttribute("height", responseBody.get("height"));
        	model.addAttribute("weight", responseBody.get("weight"));
            model.addAttribute("imageUrl", responseBody.get("image_url"));
            model.addAttribute("message", responseBody.get("message"));
            model.addAttribute("success", true);
            model.addAttribute("httpStatus", httpStatus); 
    	} catch (HttpClientErrorException | HttpServerErrorException e) {
    		// Se gestiona el error devuelto por Flask
            try {
                ObjectMapper mapper = new ObjectMapper();
                Map<String, Object> errorResponse = mapper.readValue(e.getResponseBodyAsString(), Map.class);
                Map<String, String> errorDetails = (Map<String, String>) errorResponse.get("response");

                // Se añaden como atributos del HTML
                model.addAttribute("errorCode", errorDetails.get("code"));
                model.addAttribute("message", errorDetails.get("message"));
                model.addAttribute("httpStatus", e.getStatusCode().value());
            } catch (Exception ex) {
                // Otros errores que no vienen de la API
                model.addAttribute("errorCode", "UNKNOWN_EXCEPTION");
                model.addAttribute("errorMessage", "Error inesperado: " + ex.getMessage());
                model.addAttribute("httpStatus", 520);
            }
    	}
    
    	return "exceptionsAPI";
    }
}