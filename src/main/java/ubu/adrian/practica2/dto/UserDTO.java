package ubu.adrian.practica2.dto;

/**
 * Clase de transferencia de datos de objetos User
 */
public class UserDTO {
	// Atributos (coinciden con User)
	private Long id;
    private String username;
    private String password;
    private String rol;

    /**
     * Constructor sin parámetros
     */
    public UserDTO() {}

    /**
     * Constructor con parámetros
     * 
     * @param id Id del usuario
     * @param username Nombre del usuario
     * @param password Contraseña del usuario
     * @param rol Rol de usuario
     */
    public UserDTO(Long id, String username, String password, String rol) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.rol = rol;
    }

    /**
     * Getter de Id
     * 
     * @return Id
     */
    public Long getId() {
        return id;
    }

    /**
     * Setter de Id
     * 
     * @param Id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Getter de username
     * 
     * @return username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Setter de username
     * 
     * @param username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Getter de password
     * 
     * @return password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Setter de password
     * 
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Getter de rol
     * 
     * @return rol
     */
    public String getRol() {
        return rol;
    }

    /**
     * Setter de rol
     * 
     * @param rol
     */
    public void setRol(String rol) {
        this.rol = rol;
    }
}