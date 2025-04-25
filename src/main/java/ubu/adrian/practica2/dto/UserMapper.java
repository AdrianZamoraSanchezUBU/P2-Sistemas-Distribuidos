package ubu.adrian.practica2.dto;

import org.springframework.stereotype.Component;

import ubu.adrian.practica2.model.User;

/**
 * Componente encargado de mapear objetos DTO a User y viceversa
 */
@Component
public class UserMapper {
    /**
     * Genera un UserDTO a partir de un User
     * 
     * @param User objeto de tipo usuario
     * @return UserDTO DTO del usuario
     */
	public UserDTO toDTO(User user) {
        return new UserDTO(user.getId(), user.getUsername(), null, user.getRol());
    }

	/**
     * Genera un User a partir de un UserDTO
     * 
     * @param UserDTO dto de User
     * @return User objeto de tipo usuario
     */
    public User toEntity(UserDTO dto) {
        User user = new User();
        user.setId(dto.getId());
        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword());
        user.setRol(dto.getRol());
        return user;
    }
}