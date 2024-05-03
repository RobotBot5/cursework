package com.rtumirea.KazakovIG.cursework.controllers;

import com.rtumirea.KazakovIG.cursework.domain.dto.CarDto;
import com.rtumirea.KazakovIG.cursework.domain.dto.UserDto;
import com.rtumirea.KazakovIG.cursework.domain.entities.CarEntity;
import com.rtumirea.KazakovIG.cursework.domain.entities.ServiceEntity;
import com.rtumirea.KazakovIG.cursework.domain.entities.UserEntity;
import com.rtumirea.KazakovIG.cursework.mappers.Mapper;
import com.rtumirea.KazakovIG.cursework.mappers.impl.CarMapper;
import com.rtumirea.KazakovIG.cursework.services.CarService;
import com.rtumirea.KazakovIG.cursework.services.UserService;
import lombok.extern.java.Log;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
//@RequestMapping(path = "profile")
public class CarController {

    private CarService carService;

    private UserService userService;

    private Mapper<CarEntity, CarDto> carMapper;

    private Mapper<UserEntity, UserDto> userMapper;

    public CarController(CarService carService, Mapper<CarEntity, CarDto> carMapper, UserService userService, Mapper<UserEntity, UserDto> userMapper) {
        this.carService = carService;
        this.carMapper = carMapper;
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @PreAuthorize("hasAuthority('ROLE_CLIENT')")
    @GetMapping(path = "/profile")
    public String listCars(Model model) {
        List<CarEntity> carEntities = carService.findCurrentUserCars();
        CarDto carDto = new CarDto();
        model.addAttribute("listCars", carEntities.stream()
                .map(carMapper::mapTo)
                .collect(Collectors.toList()));
        model.addAttribute("car", carDto);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userPhoneNumber = authentication.getName();
        Optional<UserEntity> userEntity = userService.findByPhoneNumber(userPhoneNumber);
        Optional<UserDto> userDto = userEntity.map(userMapper::mapTo);
        model.addAttribute("user", userDto.get());

        return "profile";
    }

    @PreAuthorize("hasAuthority('ROLE_CLIENT')")
    @PostMapping(path = "/profile/new-car")
    public String addCar(@ModelAttribute("car") CarDto carDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userPhoneNumber = authentication.getName();
        CarEntity carEntity = carMapper.mapFrom(carDto);
        carEntity.setUserEntity(userService.findByPhoneNumber(userPhoneNumber).get());
        carService.createCar(carEntity);
        return "redirect:/profile";
    }
}
