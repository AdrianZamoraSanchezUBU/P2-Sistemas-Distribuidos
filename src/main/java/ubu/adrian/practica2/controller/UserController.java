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
    
    @PostMapping("/create-user")
    public String createUser(@ModelAttribute("user") User user, BindingResult result, Authentication authentication) {
    	List<String> permitedRoles = Arrays.asList("ADMIN", "USER");
    	
    	// Obtener el usuario actualmente autenticado
        boolean isAdmin = false;
        if (authentication != null && authentication.getAuthorities() != null) {
            isAdmin = authentication.getAuthorities().stream()
                    .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ADMIN"));
        }
        
        // Si el usuario no es ADMIN, forzar el rol USER
        if (!isAdmin) {
            user.setRol("USER");
        }
    	
    	// Se comprueba que el usuario tiene contraseña
    	if (user.getPassword() == null || user.getPassword().isEmpty()) {
            result.rejectValue("password", "error.user", "La contraseña es obligatoria");
            return "register";
        }
    	
    	// Se comprueba que el usuario tiene nombre
    	if (user.getUsername() == null || user.getUsername().isEmpty()) {
            result.rejectValue("username", "error.user", "El nombre de usuario es obligatorio");
            return "register";
        }
    	
    	// Se comprueba que el rol seleccionado sea correcto
    	if (!permitedRoles.contains(user.getRol()) || user.getRol().isEmpty()) {
            result.rejectValue("rol", "error.user", "El rol debe ser correcto y no puede estar vacío");
            return "register";
        }
        
        // Se encripta la contraseña antes de guardar
    	String passwordEncriptada = passwordEncoder.encode(user.getPassword());
        user.setPassword(passwordEncriptada);
    	
    	// Se guarda al usuario en la db
        userServices.saveUser(user);
        
        if(isAdmin) {
        	return "redirect:/user-list";
        }
        
        return "redirect:/login";
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