package ubu.adrian.practica2.services;

import java.util.List;
import ubu.adrian.practica2.model.User;

public interface UserServices {
    List < User > getAllUsers();
    
    void saveUser(User employee);
    
    User getUserById(long id);
    
    void deleteEmployeeById(long id);
}
