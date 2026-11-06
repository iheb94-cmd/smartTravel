
package com.example.SmartTravel.dto;

import lombok.Data;

@Data
public class UpdatePasswordRequest {

    private String email;
    private String confirmCode;
    private String newPassword;


}
