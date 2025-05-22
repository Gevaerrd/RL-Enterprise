/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package RLEnterprise.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import RLEnterprise.dto.UserLoginDTO;
import RLEnterprise.dto.UserProfileDTO;
import RLEnterprise.dto.UserRegisterDTO;
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

    public UserProfileDTO findUserDTOByEmail(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null)
            return null;
        // Não inclua a senha no DTO por segurança
        return new UserProfileDTO(user);
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

    public boolean validLogin(UserLoginDTO userDTO) {
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

    public void save(User user) {
        userRepository.save(user);
    }

    public void saveUser(UserRegisterDTO dto) {
        User user = new User();
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setName((dto.getName()));
        userRepository.save(user);
    }

    public void setPassword(String password, User user) {
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
    }

    public PasswordEncoder getPasswordEncoder() {
        return passwordEncoder;
    }

    public User findBytwoFactorCode(String code) {
        User user = userRepository.findBytwoFactorCode(code);
        if (user != null) {
            return user;
        }
        return null;
    }
}
