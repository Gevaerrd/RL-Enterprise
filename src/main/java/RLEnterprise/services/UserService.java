/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package RLEnterprise.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public boolean userExists(Long id) {
        return userRepository.existsById(id);
    }

    public boolean deleteById(Long id) {
        if (userExists(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }
}
