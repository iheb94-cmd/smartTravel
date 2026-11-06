
package com.example.SmartTravel.mapper;

import com.example.SmartTravel.dto.CreateUserDto;
import com.example.SmartTravel.dto.UserDto;
import com.example.SmartTravel.entity.User;

public class UserMapper {

    public static UserDto mapToDto(User user) {
        if (user == null) {
            return null;
        }
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setNom(user.getNom());
        dto.setPrenom(user.getPrenom());
        dto.setEmail(user.getEmail());
        dto.setTelephone(user.getTelephone());
        dto.setRole(user.getRole());

        return dto;
    }

    public static User mapCreateUserDtoToEntity(CreateUserDto dto) {
        if (dto == null) {
            return null;
        }
        User user = new User();
        user.setNom(dto.getNom());
        user.setPrenom(dto.getPrenom());
        user.setEmail(dto.getEmail());
        user.setTelephone(dto.getTelephone());
        user.setPassword(dto.getPassword());
        user.setRole(dto.getRole());

        return user;
    }
}
