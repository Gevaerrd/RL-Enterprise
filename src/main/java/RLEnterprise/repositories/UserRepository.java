/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */

package RLEnterprise.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import RLEnterprise.entities.User;

/**
 *
 * @author Pichau
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email); // ✅ correto

    User findByEmail(String email);

    User findBytwoFactorCode(String twoFactorCode);

    User findByCpf(String cpf);

    List<User> findAll();

    List<User> findByNameContainingIgnoreCase(String name);

}
