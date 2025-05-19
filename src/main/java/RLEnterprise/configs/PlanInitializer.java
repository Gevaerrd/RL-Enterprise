/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package RLEnterprise.configs;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import RLEnterprise.entities.Plan;
import RLEnterprise.services.PlanService;

@Configuration
public class PlanInitializer {

    @Bean
    public CommandLineRunner initPlans(PlanService pr) {
        return args -> {
            if (pr.count() == 0) {
                pr.savePlan(new Plan("Plano Básico", 3, 47.0));
                pr.savePlan(new Plan("Plano Pro", 4, 97.0));
                pr.savePlan(new Plan("Plano Master", 6, 197.0));
            }
        };

    }

}
