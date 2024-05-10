package com.rtumirea.KazakovIG.cursework.controllers;

import com.rtumirea.KazakovIG.cursework.domain.dto.UserDto;
import com.rtumirea.KazakovIG.cursework.domain.entities.UserEntity;
import com.rtumirea.KazakovIG.cursework.mappers.Mapper;
import com.rtumirea.KazakovIG.cursework.services.OrderService;
import com.rtumirea.KazakovIG.cursework.services.UserService;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class AdminController {

    private UserService userService;

    private Mapper<UserEntity, UserDto> userMapper;

    private OrderService orderService;

    public AdminController(UserService userService, Mapper<UserEntity, UserDto> userMapper, OrderService orderService) {
        this.userService = userService;
        this.userMapper = userMapper;
        this.orderService = orderService;
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @GetMapping(path = "/admin/automechs")
    public String getAutomechs(Model model) {
        model.addAttribute("user", new UserDto());
        List<UserEntity> automechEntities = userService.getAutomechs();
        List<UserDto> automechDto = automechEntities.stream().map(userMapper::mapTo).collect(Collectors.toList());
        model.addAttribute("automechs", automechDto);
        return "register_automech";
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @PostMapping(path = "/admin/automechs/delete-automech")
    public String deleteAutomech(@RequestParam(name = "deletableAutomechId") Long automechId) {
        orderService.deleteAutomechById(automechId);
        return "redirect:/admin/automechs";
    }
}
