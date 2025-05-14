/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package RLEnterprise.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import RLEnterprise.dto.UserDTO;
import RLEnterprise.entities.User;
import RLEnterprise.repositories.UserRepository;

/**
 *
 * @author Pichau
 */
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public boolean emailExists(String email) {
        if (userRepository.existsByEmail(email)) {
            return true;
        }

        else {
            return false;
        }
    }

    public boolean userExists(Long id) {
        return userRepository.existsById(id);
    }

    public void deleteById(Long id) {
        if (userExists(id)) {
            userRepository.deleteById(id);
        }
    }

    public boolean validLogin(UserDTO userDTO) {
        String email = userDTO.getEmail();
        String password = userDTO.getPassword();

        if (emailExists(email)) {
            User userForCheck = userRepository.findByEmail(email);
            if (passwordEncoder.matches(password, userForCheck.getPassword())) {
                return true;
            }
            return false;
        }
        return false;
    }

    public User findByEmail(String email) {
        User user = userRepository.findByEmail(email);
        return user;
    }

    public void saveUser(UserDTO dto) {
        User user = new User();
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setName((dto.getName()));
        userRepository.save(user);
    }
}
