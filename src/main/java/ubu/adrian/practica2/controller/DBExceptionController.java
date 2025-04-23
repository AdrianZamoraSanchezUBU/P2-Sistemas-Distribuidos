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
	
	// Variables usadas para gestionar la base de datos y tabla usada
	private String database = null;
	private String table = null;
	
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
        model.addAttribute("db", database);
        model.addAttribute("table", table);
    
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
    		// Llamada a la API para establecer una conexión
            ResponseEntity<Map> response = restTemplate.getForEntity(
            		"http://flask-api:5000/api/db/connect?db=" + database + "&table=" + table, Map.class);
            
            this.database = database;
            this.table = table;

        	// Obtener el campo "response"
        	Map<String, Object> responseBody = (Map<String, Object>) response.getBody().get("response");
            
        	// Acceder a los campos dentro de "response"
        	String code = (String) responseBody.get("code");
        	String db_conection = (String) responseBody.get("db");
        	String table_conection = (String) responseBody.get("table");
        	String message = (String) responseBody.get("message");
        	
        	int httpStatus = response.getStatusCode().value();
        	
            // Se establecen los atributos pasados al HTML
            model.addAttribute("db", db_conection);
            model.addAttribute("table", table_conection);
            model.addAttribute("code", code);
            model.addAttribute("message", message);
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
    
    	return "exceptionsDB";
    }
    
    
    /**
	 * Gestiona las solicitudes de la ruta /db/error-insercion
	 * 
	 * @return pagina de excepciones
	 */
    @GetMapping("/db/error-insercion")
    public String dbErrorInsercion(Model model) {
    	try {
    		// Llamada a la API para establecer una conexión (debe no ser exitosa)
    		ResponseEntity<Map> response = restTemplate.getForEntity(
            		"http://flask-api:5000/api/db/error-insercion?db=" + database + "&table=" + table, Map.class);

        	// Obtener el campo "response"
        	Map<String, Object> responseBody = (Map<String, Object>) response.getBody().get("response");
            
        	// Acceder a los campos dentro de "response"
        	String code = (String) responseBody.get("code");
        	String message = (String) responseBody.get("message");
        	
        	int httpStatus = response.getStatusCode().value();
        	
            // Se establecen los atributos pasados al HTML
            model.addAttribute("code", code);
            model.addAttribute("db", database);
            model.addAttribute("table", table);
            model.addAttribute("message", message);
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
    
    	return "exceptionsDB";
    }
    
    /**
	 * Gestiona las solicitudes de la ruta /db/formato-incorrecto
	 * 
	 * @return pagina de excepciones
	 */
    @GetMapping("/db/formato-incorrecto")
    public String dbFormatError(Model model) {
    	try {
    		// Llamada a la API para establecer una conexión (debe no ser exitosa)
    		ResponseEntity<Map> response = restTemplate.getForEntity(
            		"http://flask-api:5000/api/db/formato-incorrecto?db=" + database + "&table=" + table, Map.class);

        	// Obtener el campo "response"
        	Map<String, Object> responseBody = (Map<String, Object>) response.getBody().get("response");
            
        	// Acceder a los campos dentro de "response"
        	String code = (String) responseBody.get("code");
        	String message = (String) responseBody.get("message");
        	
        	int httpStatus = response.getStatusCode().value();
        	
            // Se establecen los atributos pasados al HTML
            model.addAttribute("code", code);
            model.addAttribute("db", database);
            model.addAttribute("table", table);
            model.addAttribute("message", message);
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
    
    	return "exceptionsDB";
    }
    
    /**
	 * Gestiona las solicitudes de la ruta /db/table-inexistente
	 * 
	 * @return pagina de excepciones
	 */
    @GetMapping("/db/recurso-inexistente")
    public String dbMissingTable(Model model) {
    	try {
    		// Llamada a la API para establecer una conexión (debe no ser exitosa)
    		ResponseEntity<Map> response = restTemplate.getForEntity(
            		"http://flask-api:5000/api/db/recurso-inexistente?db=" + database + "&table=" + table, Map.class);

        	// Obtener el campo "response"
        	Map<String, Object> responseBody = (Map<String, Object>) response.getBody().get("response");
            
        	// Acceder a los campos dentro de "response"
        	String code = (String) responseBody.get("code");
        	String message = (String) responseBody.get("message");
        	
        	int httpStatus = response.getStatusCode().value();
        	
            // Se establecen los atributos pasados al HTML
            model.addAttribute("code", code);
            model.addAttribute("db", database);
            model.addAttribute("table", table);
            model.addAttribute("message", message);
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
    
    	return "exceptionsDB";
    }
    
    /**
	 * Gestiona las solicitudes de la ruta /db/error-query
	 * 
	 * @return pagina de excepciones
	 */
    @GetMapping("/db/error-query")
    public String dbQueryError(Model model) {
    	try {
    		// Llamada a la API para establecer una conexión (debe no ser exitosa)
    		ResponseEntity<Map> response = restTemplate.getForEntity(
            		"http://flask-api:5000/api/db/query-error?db=" + database + "&table=" + table, Map.class);

        	// Obtener el campo "response"
        	Map<String, Object> responseBody = (Map<String, Object>) response.getBody().get("response");
            
        	// Acceder a los campos dentro de "response"
        	String code = (String) responseBody.get("code");
        	String message = (String) responseBody.get("message");
        	
        	int httpStatus = response.getStatusCode().value();
        	
            // Se establecen los atributos pasados al HTML
            model.addAttribute("code", code);
            model.addAttribute("db", database);
            model.addAttribute("table", table);
            model.addAttribute("message", message);
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
    
    	return "exceptionsDB";
    }
}