/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package RLEnterprise.dto;

import java.util.ArrayList;
import java.util.List;

import RLEnterprise.entities.AfilliateCode;
import RLEnterprise.entities.AfilliateSelling;
import RLEnterprise.entities.Plan;
import RLEnterprise.entities.User;
import RLEnterprise.entities.WithdrawRequest;

/**
 *
 * @author Pichau
 */
public class UserProfileDTO {

    public String name;
    public String email;
    public Plan plan;
    public AfilliateCode afilliateCode;
    private double balance;
    private List<WithdrawRequest> withdrawRequests = new ArrayList<>();
    private List<AfilliateSelling> afilliateSellings = new ArrayList<>();

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
        this.balance = user.getBalance();
        if (user.getPlan() != null) {
            this.plan = user.getPlan();
        }

        if (user.getAfilliateCode() != null) {
            this.afilliateCode = user.getAfilliateCode();
        }
        this.withdrawRequests = user.getWithdrawRequests();
        this.afilliateSellings = user.getAfilliateSellings();

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

    public double getBalance() {
        return balance;
    }

    public List<WithdrawRequest> getWithdrawRequests() {
        return withdrawRequests;
    }

    public List<AfilliateSelling> getAfilliateSellings() {
        return afilliateSellings;
    }

}
