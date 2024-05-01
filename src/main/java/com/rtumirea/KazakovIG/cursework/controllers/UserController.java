package com.rtumirea.KazakovIG.cursework.controllers;

import com.rtumirea.KazakovIG.cursework.domain.dto.UserDto;
import com.rtumirea.KazakovIG.cursework.domain.entities.UserEntity;
import com.rtumirea.KazakovIG.cursework.mappers.Mapper;
import com.rtumirea.KazakovIG.cursework.services.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.java.Log;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@Log
public class UserController {

    private UserService userService;

    private Mapper<UserEntity, UserDto> userMapper;


    public UserController(UserService userService, Mapper<UserEntity, UserDto> userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @PostMapping(path = "/new-user")
    public String addUser(@ModelAttribute("user") UserDto userDto, HttpServletRequest request, Model model) {
        if(userService.isUserExists(userDto.getPhoneNumber())) {
            model.addAttribute("error", "Номер телефона уже зарегистрирован");
            return "register";
        }

        userDto.setRoles("ROLE_CLIENT");
        UserEntity userEntity = userMapper.mapFrom(userDto);
        userService.addUser(userEntity);
        try {
            request.login(userDto.getPhoneNumber(), userDto.getPassword());
        } catch (ServletException e) {
            log.warning("Error while login " + e);
        }
        return "redirect:/services";
    }
}
