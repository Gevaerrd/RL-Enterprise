/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package RLEnterprise.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import RLEnterprise.entities.AfilliateCode;

@Repository
public interface AfilliateCodeRepository extends JpaRepository<AfilliateCode, Long> {

    boolean existsByCode(String code);

    AfilliateCode findByCode(String code);

}
