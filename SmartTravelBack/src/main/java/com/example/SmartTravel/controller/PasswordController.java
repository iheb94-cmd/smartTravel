package com.example.SmartTravel.controller;

import com.example.SmartTravel.dto.ChangePasswordRequest;
import com.example.SmartTravel.dto.EmailDto;
import com.example.SmartTravel.dto.UpdatePasswordRequest;
import com.example.SmartTravel.service.PasswordService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/pass")
public class PasswordController {

    private final PasswordService passwordService;

    public PasswordController(PasswordService passwordService) {
        this.passwordService = passwordService;
    }

    @PostMapping("/send-code")
    public boolean sendCode(@RequestBody EmailDto emailDto) {
        passwordService.sendConfirmationCode(emailDto.getEmail());
        return true;
    }

    @PostMapping("/update-password")
    public boolean updatePassword(@RequestBody UpdatePasswordRequest request) {
        return passwordService.updatePassword(request);
    }

    @PostMapping("/change-password")
    public boolean changePassword(@RequestBody ChangePasswordRequest request) {
        return passwordService.changePassword(request);
    }
}