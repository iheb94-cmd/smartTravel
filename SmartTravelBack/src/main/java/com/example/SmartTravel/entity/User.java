package com.example.SmartTravel.entity;

import com.example.SmartTravel.enums.Role;
import jakarta.persistence.*;
import lombok.Data;


@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String nom;
    private String prenom;


    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    private String telephone;
    private Integer age;
    private String pays;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;
    private String confirmCode;



}