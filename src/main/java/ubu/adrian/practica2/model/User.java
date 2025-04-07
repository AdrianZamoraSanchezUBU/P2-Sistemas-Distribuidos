package ubu.adrian.practica2.model;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.List;

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

	/**
	 * Getter para ID
	 * 
	 * @return id identificador del usuario
	 */
	public long getId() {
		return id;
	}
	
	/**
	 * Setter para ID
	 * 
	 * @param id nuevo identificador del usuario
	 */
	public void setId(long id) {
		this.id = id;
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
	 * Setter para contraseña
	 * 
	 * @return password contraseña
	 */
	public void setPassword(String password) {
		this.password = password;
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
	
	/**
	 * Setter para el nombre de usuario
	 * 
	 * @param username nombre del usuario
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	
	/**
	 * Getter para rol de usuario
	 * 
	 * @return rol del usuario
	 */
    public String getRol() {
        return rol;
    }

    /**
	 * Setter para rol de usuario
	 * 
	 * @param rol rol del usuario
	 */
    public void setRol(String rol) {
        this.rol = rol;
    }
    
    /**
     * Devuelve el rol, que es el que ofrece autoridad de aceso
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + this.getRol()));
    }
	
	@Override
	public boolean isAccountNonExpired() {
	    return true;
	}

	@Override
	public boolean isAccountNonLocked() {
	    return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
	    return true;
	}

	@Override
	public boolean isEnabled() {
	    return true;
	}
}