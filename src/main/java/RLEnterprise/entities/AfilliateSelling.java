/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package RLEnterprise.entities;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "AfilliaSelling")
public class AfilliateSelling {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "seller_id")
    private User seller;

    private String buyerName;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime selledAt;

    private double comission;

    @ManyToOne
    @JoinColumn(name = "plan_id")
    private Plan plan;

    public AfilliateSelling() {

    }

    public AfilliateSelling(User user, LocalDateTime selledAt) {
        this.seller = user;
        this.selledAt = selledAt;
    }

    public User getUser() {
        return seller;
    }

    public void setUser(User user) {
        this.seller = user;
    }

    public LocalDateTime getSelledAt() {
        return selledAt;
    }

    public void setSelledAt(LocalDateTime selledAt) {
        this.selledAt = selledAt;
    }

    public Long getId() {
        return id;
    }

    public double getComission() {
        return comission;
    }

    public void setComission(double comission) {
        this.comission = comission;
    }

    public Plan getPlan() {
        return plan;
    }

    public void setPlan(Plan plan) {
        this.plan = plan;
    }

    public User getSeller() {
        return seller;
    }

    public void setSeller(User seller) {
        this.seller = seller;
    }

    public String getBuyerName() {
        return buyerName;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }

}
