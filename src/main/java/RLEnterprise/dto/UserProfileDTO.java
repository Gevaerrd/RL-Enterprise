/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package RLEnterprise.dto;

/**
 *
 * @author Pichau
 */
public class UserProfileDTO {

    public String name;
    public String email;

    public UserProfileDTO() {

    }

    public UserProfileDTO(String name, String email) {
        this.name = name;
        this.email = email;
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

}
