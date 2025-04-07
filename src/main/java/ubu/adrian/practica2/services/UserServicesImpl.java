package ubu.adrian.practica2.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ubu.adrian.practica2.model.User;
import ubu.adrian.practica2.repository.UserRepository;

@Service
public class UserServicesImpl implements UserServices{
	@Autowired
	private UserRepository userRepository;
	
	@Override
    public List < User > getAllUsers() {
    	
        return userRepository.findAll();
    }

	@Override
	public void saveUser(User user) {
		userRepository.save(user);
	}

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

	@Override
	public void deleteUserById(long id) {
		userRepository.deleteById(id);
	}
}
