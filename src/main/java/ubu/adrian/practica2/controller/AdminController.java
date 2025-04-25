package ubu.adrian.practica2.controller;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;

import ubu.adrian.practica2.dto.UserDTO;
import ubu.adrian.practica2.dto.UserMapper;
import ubu.adrian.practica2.model.User;
import ubu.adrian.practica2.repository.UserRepository;
import ubu.adrian.practica2.services.UserServices;

/**
 * Controlador del panel de administración
 * 
 * @author Adrián Zamora Sánchez (azs1004@alu.ubu.es)
 */
@Controller
public class AdminController {
	// Servicio de usuarios
	@Autowired
	private UserServices userServices;
	
	// Servicio de encriptación de contraseñas
	@Autowired
    private PasswordEncoder passwordEncoder;
	
	// Referencia al repositorio de usuario
	@Autowired
    private UserRepository userRepo;
	
	// Mapper de UserDTO a User
	@Autowired
	private UserMapper userMapper;

	/**
	 * Constructor que establece el userServices
	 * 
	 * @param userServices servicio de usuarios
	 */
    public AdminController(UserServices userServices) {
        this.userServices = userServices;
    }
    
    /**
	 * Gestiona las solicitudes de la ruta /admin
	 * 
	 * @return panel del administrador
	 */
    @GetMapping("/user-list")
    public String showUserList(Model model) {
    	// Lista de usuarios
    	List<UserDTO> userDTOs = userServices.getAllUsers()
                .stream()
                .map(userMapper::toDTO)
                .toList();
    	
    	// Se añaden al modelo
    	model.addAttribute("listUsers", userDTOs);
        
        return "admin";
    }
    
    /**
     * Gestiona la eliminación de usuarios
     * 
     * @param id identificador del usuario que se quiere eliminar
     * @return la lista de usuarios actualizada
     */
    @PostMapping("/remove")
    public String removeUser(@RequestParam("id") Long id) {
    	// Elimina por id
        userRepo.findById(id).ifPresent(user -> userRepo.deleteById(id));
        return "redirect:/user-list";
    }
    
    /**
	 * Gestiona las solicitudes de la ruta /updateUserData
	 * 
	 * @return panel de actualización de usuarios
	 */
    @GetMapping("/update-user-data")
    public String updateUserData(@RequestParam Long id, Model model) {
    	User user = userServices.getUserById(id);
    	
        model.addAttribute("user", user);
    	
        return "updateUserData";
    }
    
    
    /**
	 * Gestiona las actualizaciones de datos de usuarios
	 * 
	 * @return panel del usuario
	 */
    @PostMapping("/update-user")
    public String updateUser(@ModelAttribute("user") UserDTO updatedUserDTO, Model model) {
        // Busca al usuario (el cual puede no estar)
    	Optional<User> optionalUser = userRepo.findById(updatedUserDTO.getId());
        
    	// Comprueba si el usuario esta presente
    	if (optionalUser.isPresent()) {
    		// Toma los datos del usuario
            User existingUser = optionalUser.get();
            existingUser.setUsername(updatedUserDTO.getUsername());

            // Encripta la nueva contraseña
            String passwordEncriptada = passwordEncoder.encode(updatedUserDTO.getPassword());
            existingUser.setPassword(passwordEncriptada);

            existingUser.setRol(updatedUserDTO.getRol());
            
            // Lo vuelve a guardar (reescribe sus datos)
            userRepo.save(existingUser);
        } else {
        	// Devuelve mensaje de error
            model.addAttribute("error", "No se encontró un usuario con el ID especificado.");
        }

    	return "redirect:/user-list";
    }
}