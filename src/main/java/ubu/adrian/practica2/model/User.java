package ubu.adrian.practica2.model;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;

/**
 * Entidad de usuarios de la base de datos
 */
@Entity
@Table(name = "users")
public class User implements UserDetails {

	private static final long serialVersionUID = 1L;

	// ID único
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

	// Nombre de usuario único y no nula
    @Column(unique = true, nullable = false)
    private String username;

    // Contraseña no nula
    @Column(nullable = false)
    private String password;

    // Rol
    private String rol;

    /**
     * Constructor por defecto.
     */
    public User() {}

    /**
     * Constructor con parámetros
     * 
     * @param username Nombre de usuario
     * @param password Contraseña
     * @param rol      Rol del usuario
     */
    public User(String username, String password, String rol) {
        this.username = username;
        this.password = password;
        this.rol = rol;
    }

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Getter para contraseña
	 * 
	 * @return contraseña
	 */
	@Override
	public String getPassword() {
		return password;
	}

	/**
	 * Getter para el nombre de usuario
	 * 
	 * @return nombre del usuario
	 */
	@Override
	public String getUsername() {
		return username;
	}
}