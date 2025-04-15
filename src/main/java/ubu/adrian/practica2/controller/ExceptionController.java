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
public class ExceptionController {
	
	@Autowired
    private RestTemplate restTemplate;

    /**
     * Gestiona las solicitudes de la ruta /exceptions
     * 
     * @return pagina para probar las excepciones
     */
    @GetMapping("/exceptions")
    public String exceptions(Model model) {
        try {
            String flaskApiUrl = "http://flask-api:5000/api/file/list";
            ResponseEntity<Map> response = restTemplate.getForEntity(flaskApiUrl, Map.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                List<String> files = (List<String>) response.getBody().get("files");
                model.addAttribute("files", files);
            } else {
                model.addAttribute("error", "Error al obtener archivos: " + response.getStatusCode());
            }

        } catch (Exception e) {
            model.addAttribute("error", "Excepción al llamar a Flask API: " + e.getMessage());
        }

        return "exceptions";
    }


	/**
	 * Gestiona las solicitudes de la ruta /file/read
	 * 
	 * @return pagina de excepciones
	 */
    @GetMapping("/file/read")
    public String readFile(@RequestParam String filename, Model model) {    
        try {
        	// Llamada a la API con el archivo a leer
            ResponseEntity<Map> response = restTemplate.getForEntity(
                "http://flask-api:5000/api/file/read?filename=" + filename, Map.class);

            if (response.getStatusCode().is2xxSuccessful()) {
            	String content = (String) response.getBody().get("content");
                
            	// Se establecen los atributos pasados al HTML
                model.addAttribute("readSuccess", true);
                model.addAttribute("filename", filename);
                model.addAttribute("content", content);
            }
        } catch (HttpClientErrorException | HttpServerErrorException e) {
        	// Se gestiona el error devuelto por Flask
            try {
                ObjectMapper mapper = new ObjectMapper();
                Map<String, Object> errorResponse = mapper.readValue(e.getResponseBodyAsString(), Map.class);
                Map<String, String> errorDetails = (Map<String, String>) errorResponse.get("error");

                // Se añaden como atributos del HTML
                model.addAttribute("errorCode", errorDetails.get("code"));
                model.addAttribute("errorMessage", errorDetails.get("message"));
            } catch (Exception ex) {
                // Otros errores que no vienen de la API
                model.addAttribute("errorCode", "UNKNOWN_EXCEPTION");
                model.addAttribute("errorMessage", "Error inesperado: " + ex.getMessage());
            }
        }

        return "exceptions";
    }

    /**
	 * Gestiona las solicitudes de la ruta /file/write
	 * 
	 * @return pagina de excepciones
	 */
    @GetMapping("/file/write")
    public String writeFile(@RequestParam String filename, Model model) {
    	try {
    		// Llamada a la API con el archivo a escribir (le suma 1 al contenido)
            ResponseEntity<Map> response = restTemplate.getForEntity(
                "http://flask-api:5000/api/file/write?filename=" + filename, Map.class);

            int content = (int) response.getBody().get("content");
            
            // Se establecen los atributos pasados al HTML
            model.addAttribute("writeSuccess", true);
            model.addAttribute("filename", filename);
            model.addAttribute("content", content);
            
    	} catch (HttpClientErrorException | HttpServerErrorException e) {
        	// Se gestiona el error devuelto por Flask
            try {
                ObjectMapper mapper = new ObjectMapper();
                Map<String, Object> errorResponse = mapper.readValue(e.getResponseBodyAsString(), Map.class);
                Map<String, String> errorDetails = (Map<String, String>) errorResponse.get("error");

                // Se añaden como atributos del HTML
                model.addAttribute("errorCode", errorDetails.get("code"));
                model.addAttribute("errorMessage", errorDetails.get("message"));
            } catch (Exception ex) {
                // Otros errores que no vienen de la API
                model.addAttribute("errorCode", "UNKNOWN_EXCEPTION");
                model.addAttribute("errorMessage", "Error inesperado: " + ex.getMessage());
            }
        }

        return "exceptions";
    }
}