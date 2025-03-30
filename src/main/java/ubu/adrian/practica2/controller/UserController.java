package ubu.adrian.practica2.controller;

import org.springframework.beans.factory.annotation.Autowired;
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
    	// TODO logica de login
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
    
    @PostMapping("/createUser")
    public String createUser(@ModelAttribute("user") User user, BindingResult result) {
    	if (user.getPassword() == null || user.getPassword().isEmpty()) {
            result.rejectValue("password", "error.user", "La contraseña es obligatoria");
            return "register";
        }
        
        // Se encripta la contraseña antes de guardar
    	String passwordEncriptada = passwordEncoder.encode(user.getPassword());
        user.setPassword(passwordEncriptada);
    	
    	// Se guarda al usuario en la db
        userServices.saveUser(user);
        return "redirect:/userList";
    }
    
    /**
	 * Gestiona las solicitudes de la ruta /user
	 * 
	 * @return panel del usuario
	 */
    @GetMapping("/user")
    public String user() {
        return "user";
    }
}