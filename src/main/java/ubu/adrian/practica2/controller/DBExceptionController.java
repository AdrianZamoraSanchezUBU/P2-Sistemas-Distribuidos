package ubu.adrian.practica2.controller;


import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.ui.Model;

/**
 * Controlador de la página de excepciones
 */
@Controller
public class DBExceptionController {
	
	@Autowired
    private RestTemplate restTemplate;

	/**
	 * Gestiona las solicitudes de la ruta /db/connection
	 * 
	 * @return pagina de excepciones
	 */
    @GetMapping("/db/menu")
    public String dbMenu(Model model) {
    	// Se establecen los atributos pasados al HTML
        model.addAttribute("db", false);
    
    	return "exceptionsDB";
    }
    
    /**
	 * Gestiona las solicitudes de la ruta /db/connection
	 * 
	 * @return pagina de excepciones
	 */
    @GetMapping("/db/connection")
    public String dbConection(@RequestParam String database, @RequestParam String table, Model model) {
    	try {
    		// Llamada a la API para establecer una conexión (debe no ser exitosa)
            ResponseEntity<Map> response = restTemplate.getForEntity(
            		"http://flask-api:5000/api/db/connect?db=" + database + "&table=" + table, Map.class);

        	// Obtener el campo "response"
        	Map<String, Object> responseBody = (Map<String, Object>) response.getBody().get("response");
            
        	// Acceder a los campos dentro de "response"
        	String code = (String) responseBody.get("code");
        	String db_conection = (String) responseBody.get("db");
        	String table_conection = (String) responseBody.get("table");
        	String message = (String) responseBody.get("message");
        	
            // Se establecen los atributos pasados al HTML
            model.addAttribute("db", db_conection);
            model.addAttribute("table", table_conection);
            model.addAttribute("code", code);
            model.addAttribute("message", message);
            model.addAttribute("success", true);
            
    	} catch (HttpClientErrorException | HttpServerErrorException e) {
    		// Se gestiona el error devuelto por Flask
            try {
                ObjectMapper mapper = new ObjectMapper();
                Map<String, Object> errorResponse = mapper.readValue(e.getResponseBodyAsString(), Map.class);
                Map<String, String> errorDetails = (Map<String, String>) errorResponse.get("response");

                // Se añaden como atributos del HTML
                model.addAttribute("errorCode", errorDetails.get("code"));
                model.addAttribute("message", errorDetails.get("message"));
            } catch (Exception ex) {
                // Otros errores que no vienen de la API
                model.addAttribute("errorCode", "UNKNOWN_EXCEPTION");
                model.addAttribute("errorMessage", "Error inesperado: " + ex.getMessage());
            }
    	}
    
    	return "exceptionsDB";
    }
}