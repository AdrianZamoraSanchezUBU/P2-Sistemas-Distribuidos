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
 * Controlador del panel de excepciones con bases de datos
 * 
 * @author Adrián Zamora Sánchez (azs1004@alu.ubu.es)
 */
@Controller
public class DBExceptionController {
	
	// Variables usadas para gestionar la base de datos y tabla usada
	private String database = null;
	private String table = null;
	
	// Plantilla REST
	@Autowired
    private RestTemplate restTemplate;
	
	/**
	 * Gestiona las solicitudes, unificadas en este método pues todas son 
	 * requests a bases de datos
	 * 
	 * @param endpoint de llamada de la API
	 * @param model modelo que se debe devolver
	 * @return página de excepciones de bases de datos
	 */
	private String handleDbRequest(String endpoint, Model model) {
        try {
            // Llamada a la API con los parámetros de la solicitud
            ResponseEntity<Map> response = restTemplate.getForEntity(
                "http://flask-api:5000/api/db/" + endpoint + "?db=" + database + "&table=" + table, Map.class);

            // Se obtiene el campo response de la respuesta de la API
            Map<String, Object> responseBody = (Map<String, Object>) response.getBody().get("response");

            // Se extraen los valores de la respuesta
            String code = (String) responseBody.get("code");
            String message = (String) responseBody.get("message");
            int httpStatus = response.getStatusCode().value();

            // Establecer los atributos para la vista
            model.addAttribute("code", code);
            model.addAttribute("db", database);
            model.addAttribute("table", table);
            model.addAttribute("message", message);
            model.addAttribute("success", true);
            model.addAttribute("httpStatus", httpStatus);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            // Manejar error de la API
            try {
                ObjectMapper mapper = new ObjectMapper();
                Map<String, Object> errorResponse = mapper.readValue(e.getResponseBodyAsString(), Map.class);
                Map<String, String> errorDetails = (Map<String, String>) errorResponse.get("response");

                model.addAttribute("errorCode", errorDetails.get("code"));
                model.addAttribute("message", errorDetails.get("message"));
                model.addAttribute("httpStatus", e.getStatusCode().value());
            } catch (Exception ex) {
                // Otros errores
                model.addAttribute("errorCode", "UNKNOWN_EXCEPTION");
                model.addAttribute("errorMessage", "Error inesperado: " + ex.getMessage());
                model.addAttribute("httpStatus", 520);
            }
        }

        return "exceptionsDB";
    }
	

	/**
	 * Gestiona las solicitudes de la ruta /db/menu
	 * 
	 * @return pagina de conexion a la base de datos
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
	 * @return pagina con resultado de la conexion
	 */
    @GetMapping("/db/connection")
    public String dbConection(@RequestParam String database, @RequestParam String table, Model model) {
        this.database = database;
        this.table = table;
        return handleDbRequest("connect", model);
    }

    /**
	 * Gestiona las solicitudes de la ruta /db/error-insercion
	 * 
	 * @return pagina con resultado de la operacion
	 */
    @GetMapping("/db/error-insercion")
    public String dbErrorInsercion(Model model) {
        return handleDbRequest("error-insercion", model);
    }

    /**
	 * Gestiona las solicitudes de la ruta /db/formato-incorrecto
	 * 
	 * @return pagina con resultado de la operacion
	 */
    @GetMapping("/db/formato-incorrecto")
    public String dbFormatError(Model model) {
        return handleDbRequest("formato-incorrecto", model);
    }

    /**
	 * Gestiona las solicitudes de la ruta /db/recurso-inexistente
	 * 
	 * @return pagina con resultado de la operacion
	 */
    @GetMapping("/db/recurso-inexistente")
    public String dbMissingTable(Model model) {
        return handleDbRequest("recurso-inexistente", model);
    }

    /**
	 * Gestiona las solicitudes de la ruta /db/error-query
	 * 
	 * @return pagina con resultado de la query
	 */
    @GetMapping("/db/error-query")
    public String dbQueryError(Model model) {
        return handleDbRequest("query-error", model);
    }
}