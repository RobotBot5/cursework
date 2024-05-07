package com.rtumirea.KazakovIG.cursework.controllers;

import com.rtumirea.KazakovIG.cursework.domain.dto.UserDto;
import com.rtumirea.KazakovIG.cursework.domain.entities.ServiceEntity;
import com.rtumirea.KazakovIG.cursework.domain.enums.ServiceType;
import com.rtumirea.KazakovIG.cursework.repositories.ServiceRepository;
import com.rtumirea.KazakovIG.cursework.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    private UserService userService;

    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(path = "/login")
    public String login() {
        return "login";
    }

    @GetMapping(path = "/register")
    public String register(Model model) {
        UserDto userDto = new UserDto();
        model.addAttribute("user", userDto);

//        userService.addAutoMech("1", "Алексей", "1");
//        userService.addAutoMech("2", "Дмитрий", "1");
//        userService.addAutoMech("3", "Владислав", "1");


        return "register";
    }

}
