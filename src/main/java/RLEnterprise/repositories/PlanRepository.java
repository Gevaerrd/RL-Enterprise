/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */

package RLEnterprise.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import RLEnterprise.entities.Plan;

@Repository
public interface PlanRepository extends JpaRepository<Plan, Long> {

    Plan findByName(String name);

    List<Plan> findAll();

}
