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
 */
@Controller
public class AdminController {
	
	@Autowired
	private UserServices userServices;
	
	@Autowired
    private PasswordEncoder passwordEncoder;
	
	@Autowired
    private UserRepository userRepo;
	
	@Autowired
	private UserMapper userMapper;

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
    	List<UserDTO> userDTOs = userServices.getAllUsers()
                .stream()
                .map(userMapper::toDTO)
                .toList();
    	
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
    	System.out.println("Se ha eliminado el usuario: " + Long.toString(id));
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
        
    	Optional<User> optionalUser = userRepo.findById(updatedUserDTO.getId());
        
    	if (optionalUser.isPresent()) {
            User existingUser = optionalUser.get();
            existingUser.setUsername(updatedUserDTO.getUsername());

            String passwordEncriptada = passwordEncoder.encode(updatedUserDTO.getPassword());
            existingUser.setPassword(passwordEncriptada);

            existingUser.setRol(updatedUserDTO.getRol());

            userRepo.save(existingUser);
        } else {
            model.addAttribute("error", "No se encontró un usuario con el ID especificado.");
        }

    	
    	return "redirect:/user-list";
    }
}