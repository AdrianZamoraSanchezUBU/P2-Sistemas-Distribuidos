package ubu.adrian.practica2.repository;

import ubu.adrian.practica2.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio de la entidad User
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	
	/**
     * Busca un usuario por su nombre de usuario
     * 
     * @param username nombre de usuario a buscar
     * @return el usuario encontrado o null
     */
    User findByUsername(String username);
}