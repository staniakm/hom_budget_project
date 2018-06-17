package com.mariusz.home_budget.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "application_users")
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "email")
    private String name;
    private String password;
    @Column(name = "enabled")
    private boolean enabled;

    public AppUser(){
        this.enabled = false;
    }
}
