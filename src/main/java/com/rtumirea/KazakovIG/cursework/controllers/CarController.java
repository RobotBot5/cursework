package com.rtumirea.KazakovIG.cursework.controllers;

import com.rtumirea.KazakovIG.cursework.domain.dto.CarDto;
import com.rtumirea.KazakovIG.cursework.domain.dto.ScheduleDto;
import com.rtumirea.KazakovIG.cursework.domain.dto.UserDto;
import com.rtumirea.KazakovIG.cursework.domain.dto.order.OrderDtoTo;
import com.rtumirea.KazakovIG.cursework.domain.entities.*;
import com.rtumirea.KazakovIG.cursework.domain.enums.OrderStatus;
import com.rtumirea.KazakovIG.cursework.mappers.Mapper;
import com.rtumirea.KazakovIG.cursework.services.CarService;
import com.rtumirea.KazakovIG.cursework.services.OrderService;
import com.rtumirea.KazakovIG.cursework.services.ScheduleService;
import com.rtumirea.KazakovIG.cursework.services.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Controller
//@RequestMapping(path = "profile")
public class CarController {

    private CarService carService;

    private UserService userService;

    private Mapper<CarEntity, CarDto> carMapper;

    private Mapper<UserEntity, UserDto> userMapper;

    private OrderService orderService;

    private Mapper<OrderEntity, OrderDtoTo> orderMapperTo;

    private ScheduleService scheduleService;

    private Mapper<ScheduleEntity, ScheduleDto> scheduleMapper;

    public CarController(CarService carService, Mapper<CarEntity,
            CarDto> carMapper, UserService userService,
                         Mapper<UserEntity, UserDto> userMapper, OrderService orderService,
                         Mapper<OrderEntity, OrderDtoTo> orderMapperTo,
                         ScheduleService scheduleService,
                         Mapper<ScheduleEntity, ScheduleDto> scheduleMapper) {
        this.carService = carService;
        this.carMapper = carMapper;
        this.userService = userService;
        this.userMapper = userMapper;
        this.orderService = orderService;
        this.orderMapperTo = orderMapperTo;
        this.scheduleService = scheduleService;
        this.scheduleMapper = scheduleMapper;
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

        List<OrderEntity> clientOrdersEntitiesWithPendingStatus = orderService.findByCurrentClientAndStatus(OrderStatus.PENDING);
        List<OrderDtoTo> clientOrdersDtoWithPendingStatus = clientOrdersEntitiesWithPendingStatus
                .stream().map(orderMapperTo::mapTo)
                .collect(Collectors.toList());
        model.addAttribute("ordersPending", clientOrdersDtoWithPendingStatus);

        List<Integer> totalPrices = new ArrayList<>();
        for (OrderDtoTo order : clientOrdersDtoWithPendingStatus) {
            int totalPrice = order.getServiceEntity().stream()
                    .mapToInt(ServiceEntity::getPrice)
                    .sum();
            totalPrices.add(totalPrice);
        }
        model.addAttribute("totals", totalPrices);

        model.addAttribute("statusesTypeNames", getStatusesTypeNames());

        List<ScheduleEntity> freeSlots = scheduleService.getFreeSlots();
        model.addAttribute("freeSlots", freeSlots);
        Set<LocalDate> freeSlotsDays = freeSlots.stream()
                .map(ScheduleEntity::getDay)
                .collect(Collectors.toSet());
        model.addAttribute("freeSlotsDays", freeSlotsDays);

        List<OrderEntity> clientOrdersEntitiesWithSchedulingStatus = orderService.findByCurrentClientAndStatus(OrderStatus.AWAITING_SCHEDULING);
        List<OrderDtoTo> clientOrdersDtoWithSchedulingStatus = clientOrdersEntitiesWithSchedulingStatus
                .stream().map(orderMapperTo::mapTo)
                .collect(Collectors.toList());
        model.addAttribute("ordersScheduling", clientOrdersDtoWithSchedulingStatus);

        List<OrderEntity> clientOrdersEntitiesWithWaitingCarStatus = orderService.findByCurrentClientAndStatus(OrderStatus.AWAITING_CAR);
        List<OrderDtoTo> clientOrdersDtoWithWaitingCarStatus = clientOrdersEntitiesWithWaitingCarStatus
                .stream().map(orderMapperTo::mapTo)
                .collect(Collectors.toList());
        model.addAttribute("ordersWaitingCar", clientOrdersDtoWithWaitingCarStatus);

        model.addAttribute("clientOrdersSlots", scheduleService.getCurrentClientsSlots());

        return "profile";
    }

    @PreAuthorize("hasAuthority('ROLE_CLIENT')")
    @PostMapping(path = "/profile/schedule/book")
    public String bookSchedule(@RequestParam("slotId") Long slotId,
                               @RequestParam("scheduleOrderChoice") Long orderId) {
        scheduleService.bookSlot(slotId, orderId);
        return "redirect:/profile";
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

    public Map<String, String> getStatusesTypeNames() {
        Map<String, String> statusesTypeNames = new HashMap<>();
        statusesTypeNames.put("PENDING", "В обработке");
        statusesTypeNames.put("WAITING_CAR", "Ожидание машины");
        statusesTypeNames.put("IN_PROGRESS", "В работе");
        statusesTypeNames.put("COMPLETED", "Завершено");
        statusesTypeNames.put("DIESEL_ENGINE_REPAIR", "Ремонт дизельных двигателей");
        return statusesTypeNames;
    }
}
