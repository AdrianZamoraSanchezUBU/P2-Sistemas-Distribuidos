package ubu.adrian.practica2.controller;


import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
 * Controlador del panel de excepciones en el manejo de archivos
 * 
 * @author Adrián Zamora Sánchez (azs1004@alu.ubu.es)
 */
@Controller
public class FileExceptionController {
	
	@Autowired
    private RestTemplate restTemplate;

    /**
     * Gestiona las solicitudes de la ruta /file/list
     * 
     * @param model Modelo en el que se insertan los datos
     * @return página con listado de archivos
     */
    @GetMapping("/file/list")
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

        return "exceptionsFiles";
    }
    
    /**
     * Método que gestiona las solicitudes de interaccion (lectura o escritura) con archivos
     * 
     * @param endpoint String que indica si se lee o escribe
     * @param filename Nombre del archivo con el que se interactua
     * @param model Modelo en el que se insertan datos
     * @return página de excepciones donde se pueden ver los resultados de la llamada
     */
    public String fileInteractionHanlder(String endpoint, String filename, Model model) {
    	try {
        	// Llamada a la API con el archivo a leer
            ResponseEntity<Map> response = restTemplate.getForEntity(
                "http://flask-api:5000/api/file/" + endpoint + "?filename=" + filename, Map.class);

            if (response.getStatusCode().is2xxSuccessful()) {
            	String content = String.valueOf(response.getBody().get("content"));
                
            	int httpStatus = response.getStatusCode().value();
            	
            	// Se establecen los atributos pasados al HTML
                model.addAttribute("success", true);
                model.addAttribute("filename", filename);
                model.addAttribute("content", content);
                model.addAttribute("httpStatus", String.valueOf(httpStatus));
            }
        } catch (HttpClientErrorException | HttpServerErrorException e) {
        	// Se gestiona el error devuelto por Flask
            try {
                ObjectMapper mapper = new ObjectMapper();
                Map<String, Object> errorResponse = mapper.readValue(e.getResponseBodyAsString(), Map.class);
                Map<String, String> errorDetails = (Map<String, String>) errorResponse.get("response");
                
                // Se añaden como atributos del HTML
                model.addAttribute("errorCode", errorDetails.get("code"));
                model.addAttribute("errorMessage", errorDetails.get("message"));
                model.addAttribute("httpStatus", e.getStatusCode().value());
            } catch (Exception ex) {
                // Otros errores que no vienen de la API
                model.addAttribute("errorCode", "UNKNOWN_EXCEPTION");
                model.addAttribute("errorMessage", "Error inesperado: " + ex.getMessage());
                model.addAttribute("httpStatus", 520);
            }
        }

        return "exceptionsFiles";
    }


	/**
	 * Gestiona las solicitudes de la ruta /file/read
	 * 
	 * @param filename Nombre del fichero a leer
	 * @param model Modelo en el que se insertan datos
	 * @return solicitudes de lectura de archivos
	 */
    @GetMapping("/file/read")
    public String readFile(@RequestParam String filename, Model model) {    
    	return fileInteractionHanlder("read", filename, model);
    }

    /**
	 * Gestiona las solicitudes de la ruta /file/write
	 * 
	 * @param filename Nombre del fichero a escribir
	 * @param model Modelo en el que se insertan datos
	 * @return solicitudes de modificación de archivos
	 */
    @PostMapping("/file/write")
    public String writeFile(@RequestParam String filename, Model model) {
    	return fileInteractionHanlder("write", filename, model);
    }
}