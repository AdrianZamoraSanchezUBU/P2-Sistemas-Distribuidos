package ubu.adrian.practica2.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import ubu.adrian.practica2.services.UserServices;

/**
 * Controlador del panel de administración
 */
@Controller
public class AdminController {
	
	@Autowired
	private UserServices userServices;

    public AdminController(UserServices userServices) {
        this.userServices = userServices;
    }
    
    /**
	 * Gestiona las solicitudes de la ruta /admin
	 * 
	 * @return panel del administrador
	 */
    @GetMapping("/admin")
    public String showUserList(Model model) {
        model.addAttribute("listUsers", userServices.getAllUsers());
        
        return "admin";
    }
}