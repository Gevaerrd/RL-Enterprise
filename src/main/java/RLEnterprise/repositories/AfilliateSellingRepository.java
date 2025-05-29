/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */

package RLEnterprise.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import RLEnterprise.entities.AfilliateSelling;
import RLEnterprise.entities.Plan;
import RLEnterprise.entities.User;

@Repository
public interface AfilliateSellingRepository extends JpaRepository<AfilliateSelling, Long> {
    boolean existsBySellerAndBuyerNameAndPlan(User seller, String buyerName, Plan plan);

}
