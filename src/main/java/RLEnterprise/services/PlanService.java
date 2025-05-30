/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package RLEnterprise.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import RLEnterprise.entities.Plan;
import RLEnterprise.repositories.PlanRepository;

@Service
public class PlanService {

    @Autowired
    PlanRepository pr;

    public Plan savePlan(Plan plan) {
        return pr.save(plan);
    }

    public List<Plan> findAllPlans() {
        return pr.findAll();
    }

    public Plan findById(Long id) {
        return pr.findById(id).orElse(null);
    }

    public long count() {
        return pr.count();
    }

    public void deletePlan(Long id) {
        if (!pr.existsById(id)) {
            throw new RuntimeException("Plano não encontrado com ID: " + id);
        }
        pr.deleteById(id);
    }

    public Plan findByName(String name) {
        return pr.findByName(name);
    }

    public List<Plan> findAll() {
        return pr.findAll();
    }

}
