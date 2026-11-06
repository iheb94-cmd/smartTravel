package com.example.SmartTravel.dto;

import com.example.SmartTravel.enums.Role;
import lombok.Data;

@Data
public class UserDto {

    private Long id;
    private String nom;
    private String prenom;
    private String email;
    private String telephone;
    private Role role;
}
