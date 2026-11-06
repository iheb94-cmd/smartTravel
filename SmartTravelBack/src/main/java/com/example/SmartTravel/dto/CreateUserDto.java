
package com.example.SmartTravel.dto;

import com.example.SmartTravel.enums.Role;
import lombok.Data;

@Data
public class CreateUserDto {

    private String nom;
    private String prenom;
    private String email;
    private String password;
    private String telephone;
    private Role role;
}
