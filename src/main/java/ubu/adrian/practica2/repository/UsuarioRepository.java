package ubu.adrian.practica2.repository;

import ubu.adrian.practica2.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio de la entidad User
 */
public interface UsuarioRepository extends JpaRepository<User, Long> {
	
	/**
     * Busca un usuario por su nombre de usuario
     * 
     * @param username nombre de usuario a buscar
     * @return el usuario encontrado o null
     */
    User findByUsername(String username);
}