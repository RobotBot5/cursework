package com.rtumirea.KazakovIG.cursework.controllers;

import com.rtumirea.KazakovIG.cursework.domain.dto.UserDto;
import com.rtumirea.KazakovIG.cursework.domain.entities.UserEntity;
import com.rtumirea.KazakovIG.cursework.mappers.Mapper;
import com.rtumirea.KazakovIG.cursework.services.ScheduleService;
import com.rtumirea.KazakovIG.cursework.services.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.java.Log;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

@Controller
@Log
public class UserController {

    private UserService userService;

    private Mapper<UserEntity, UserDto> userMapper;

    private ScheduleService scheduleService;


    public UserController(UserService userService, Mapper<UserEntity, UserDto> userMapper,
                          ScheduleService scheduleService) {
        this.userService = userService;
        this.userMapper = userMapper;
        this.scheduleService = scheduleService;
    }

    @PostMapping(path = "/new-user")
    public String addUser(@ModelAttribute("user") UserDto userDto, HttpServletRequest request, RedirectAttributes redirectAttributes,
                          @RequestParam(name = "password_check") String passwordCheck) {
        if(userService.isUserExists(userDto.getPhoneNumber())) {
            redirectAttributes.addFlashAttribute("error", "Номер телефона уже зарегистрирован");
            return "redirect:/register";
        }
        if(!userDto.getPassword().equals(passwordCheck)) {
            redirectAttributes.addFlashAttribute("error", "Пароль должен совпадать");
            return "redirect:/admin/automechs";
        }

        userDto.setRoles("ROLE_CLIENT");
        UserEntity userEntity = userMapper.mapFrom(userDto);
        userService.addUser(userEntity);
        try {
            request.login(userDto.getPhoneNumber(), userDto.getPassword());
        } catch (ServletException e) {
            log.warning("Error while login " + e);
        }
        return "redirect:/profile";
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping(path = "/new-automech")
    public String addAutomech(@ModelAttribute("user") UserDto userDto, HttpServletRequest request, RedirectAttributes redirectAttributes,
                              @RequestParam(name = "password_check") String passwordCheck) {
        if(userService.isUserExists(userDto.getPhoneNumber())) {
            redirectAttributes.addFlashAttribute("error", "Номер телефона уже зарегистрирован");
            return "redirect:/admin/automechs";
        }
        if(!userDto.getPassword().equals(passwordCheck)) {
            redirectAttributes.addFlashAttribute("error", "Пароль должен совпадать");
            return "redirect:/admin/automechs";
        }

        userDto.setRoles("ROLE_AUTOMECH");
        userDto.setOrdersNum(0);
        UserEntity userEntity = userMapper.mapFrom(userDto);
        userService.addUser(userEntity);
        return "redirect:/admin/automechs";
    }

    @GetMapping(path = "/login")
    public String login() {
        return "login";
    }

    @GetMapping(path = "/register")
    public String register(Model model) {
        UserDto userDto = new UserDto();
        model.addAttribute("user", userDto);

//        scheduleService.generateScheduleForMonth(LocalDate.of(2024, 5, 1), 120);
//        userService.createAdmin("admin");



        return "register";
    }
}
