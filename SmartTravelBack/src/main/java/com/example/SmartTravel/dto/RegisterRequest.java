package com.example.SmartTravel.dto;

import lombok.Data;

@Data
public class RegisterRequest {
    private String nom;
    private String prenom;
    private Integer age;
    private String pays;
    private String telephone;
    private String email;
    private String password;
}