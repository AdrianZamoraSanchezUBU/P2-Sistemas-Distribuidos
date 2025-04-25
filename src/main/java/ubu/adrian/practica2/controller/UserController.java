package ubu.adrian.practica2.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import ubu.adrian.practica2.dto.UserDTO;
import ubu.adrian.practica2.model.User;
import ubu.adrian.practica2.services.UserServices;

/**
 * Controlador de las páginas relacionadas
 * con la gestión de usuarios
 */
@Controller
public class UserController {
	@Autowired
	private UserServices userServices;
	
	@Autowired
    private PasswordEncoder passwordEncoder;
    
    public UserController(UserServices userServices) {
        this.userServices = userServices;
    }

	/**
	 * Gestiona las solicitudes de la ruta /login
	 * 
	 * @return página de login
	 */
    @GetMapping("/login")
    public String login() {
        return "login";
    }
    
    /**
	 * Gestiona las solicitudes de la ruta /register
	 * 
	 * @return página de registro
	 */
    @GetMapping("/register")
    public String register(Model model) {
    	User user = new User();
    	
        model.addAttribute("user", user);
        
        return "register";
    }
    
    /**
     * Gestiona las solicitudes de la ruta /create-user
	 * 
	 * Crea un nuevo usuario a partir de los datos obtenidos
	 * 
	 * @return página de registro o user-list
     */
    @PostMapping("/create-user")
    public String createUser(@ModelAttribute("user") UserDTO userDTO, BindingResult result, Authentication authentication) {
    	List<String> permitedRoles = Arrays.asList("ADMIN", "USER");
    	
    	// Obtener el usuario actualmente autenticado
        boolean isAdmin = false;
        if (authentication != null && authentication.getAuthorities() != null) {
            isAdmin = authentication.getAuthorities().stream()
                    .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ADMIN"));
        }
    	
    	// Se comprueba que el usuario tiene contraseña
        if (userDTO.getPassword() == null || userDTO.getPassword().isEmpty()) {
            result.rejectValue("password", "error.user", "La contraseña es obligatoria");
            return "register";
        }
    	
    	// Se comprueba que el usuario tiene nombre
        if (userDTO.getUsername() == null || userDTO.getUsername().isEmpty()) {
            result.rejectValue("username", "error.user", "El nombre de usuario es obligatorio");
            return "register";
        }
    	
        // Si el usuario no es ADMIN, forzar el rol USER
        String rolFinal = isAdmin ? userDTO.getRol() : "USER";
        
        // Se comprueba que el rol seleccionado sea correcto
        if (!permitedRoles.contains(rolFinal)) {
            result.rejectValue("rol", "error.user", "Rol no permitido");
            return "register";
        }
        
        // Se genera el usuario desde el DTO
    	User user = new User();
    	user.setUsername(userDTO.getUsername());
        user.setRol(rolFinal);
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));  
        
    	// Se guarda al usuario en la db
        userServices.saveUser(user);
        
        // Si lo crea el admin devuelve user list, sino pagina de login
        return isAdmin ? "redirect:/user-list" : "redirect:/login";
    }
    
    /**
	 * Gestiona las solicitudes de la ruta /user-home
	 * 
	 * @return panel del usuario
	 */
    @GetMapping("/user-home")
    public String user() {
        return "user";
    }
}