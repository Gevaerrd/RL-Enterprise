/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package RLEnterprise.dto;

import RLEnterprise.entities.AfilliateCode;
import RLEnterprise.entities.Plan;
import RLEnterprise.entities.User;

/**
 *
 * @author Pichau
 */
public class UserProfileDTO {

    public String name;
    public String email;
    public Plan plan;
    public AfilliateCode afilliateCode;

    public UserProfileDTO() {

    }

    public UserProfileDTO(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public UserProfileDTO(User user) {
        this.name = user.getName();
        updateFirstName();
        this.email = user.getEmail();
        if (user.getPlan() != null) {
            this.plan = user.getPlan();
        }

        if (user.getAfilliateCode() != null) {
            this.afilliateCode = user.getAfilliateCode();
        }

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void updateFirstName() {
        if (name != null && !name.isBlank()) {
            this.name = name.trim().split(" ")[0];
        } else {
            this.name = "";
        }
    }

    public String getFirstName() {
        if (name == null || name.isBlank()) {
            updateFirstName();
        }
        return name;
    }

    public Plan getPlan() {
        return plan;
    }

    public void setPlan(Plan plan) {
        this.plan = plan;
    }

    public AfilliateCode getAfilliateCode() {
        return afilliateCode;
    }

    public void setAfilliateCode(AfilliateCode afilliateCode) {
        this.afilliateCode = afilliateCode;
    }

}
