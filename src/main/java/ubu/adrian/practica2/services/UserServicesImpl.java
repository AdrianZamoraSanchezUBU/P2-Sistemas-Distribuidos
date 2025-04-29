package ubu.adrian.practica2.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ubu.adrian.practica2.model.User;
import ubu.adrian.practica2.repository.UserRepository;

/**
 * Implementación de la interfaz UserServices
 */
@Service
public class UserServicesImpl implements UserServices{
	@Autowired
	private UserRepository userRepository;
	
	/**
	 * Devuelve una lista de todos los usuarios
	 * 
	 * @return List<User> Lista de todos los usuario encontrados
	 */
	@Override
    public List < User > getAllUsers() {
        return userRepository.findAll();
    }

	/**
	 * Guarda al usuario en la base de datos
	 * 
	 * @param user Usuario que se quiere guardar
	 */
	@Override
	public void saveUser(User user) {
		userRepository.save(user);
	}

	/**
	 * Devuelve un usuario especificado
	 * 
	 * @param id identificador del usuario que se busca
	 * @throws RuntimeException excepción lanzada cuando no se encuentra al usuario
	 * @return Usuario que se ha encontrado
	 */
	@Override
	public User getUserById(long id) {
		Optional < User > optional = userRepository.findById(id);
		
        User user = null;
        
        if (optional.isPresent()) {
            user = optional.get();
        } else {
            throw new RuntimeException("No existe el usuario con ID: " + id);
        }
        return user;
	}

	/**
	 * Elimina al usuario especificado
	 * 
	 * @param id Identificador del usuario que se desea eliminar
	 */
	@Override
	public void deleteUserById(long id) {
		userRepository.deleteById(id);
	}
}
