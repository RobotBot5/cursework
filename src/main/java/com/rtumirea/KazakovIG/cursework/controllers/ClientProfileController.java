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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class ClientProfileController {

    private CarService carService;

    private UserService userService;

    private Mapper<CarEntity, CarDto> carMapper;

    private Mapper<UserEntity, UserDto> userMapper;

    private OrderService orderService;

    private Mapper<OrderEntity, OrderDtoTo> orderMapperTo;

    private ScheduleService scheduleService;

    private Mapper<ScheduleEntity, ScheduleDto> scheduleMapper;

    public ClientProfileController(CarService carService, Mapper<CarEntity,
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
    public String getProfile(Model model) {
        List<CarEntity> carEntities = carService.findCurrentUserCars();
        CarDto carDto = new CarDto();
        model.addAttribute("listCars", carEntities.stream()
                .map(carMapper::mapTo)
                .collect(Collectors.toList()));
        model.addAttribute("car", carDto);

        model.addAttribute("currentYear", LocalDate.now().getYear());

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
        freeSlots.sort(Comparator.comparing(ScheduleEntity::getId));
        model.addAttribute("freeSlots", freeSlots);

        List<LocalDate> freeSlotsDays = freeSlots.stream()
                .map(ScheduleEntity::getDay).distinct().collect(Collectors.toList());
        model.addAttribute("freeSlotsDays", freeSlotsDays);

        List<OrderEntity> clientOrdersEntitiesWithSchedulingStatus = orderService.findByCurrentClientAndStatus(OrderStatus.AWAITING_SCHEDULING);
        List<OrderDtoTo> clientOrdersDtoWithSchedulingStatus = clientOrdersEntitiesWithSchedulingStatus
                .stream().map(orderMapperTo::mapTo)
                .collect(Collectors.toList());
        model.addAttribute("ordersScheduling", clientOrdersDtoWithSchedulingStatus);

        List<ScheduleEntity> clientSlotsEntities = scheduleService.getCurrentClientSlots();
        List<ScheduleDto> clientSlotsDto = clientSlotsEntities
                .stream().map(scheduleMapper::mapTo)
                .collect(Collectors.toList());
        clientSlotsDto.sort(Comparator.comparing(ScheduleDto::getId));
        model.addAttribute("clientSlots", clientSlotsDto);

        return "client/profile";
    }

    @PreAuthorize("hasAuthority('ROLE_CLIENT')")
    @PostMapping(path = "/profile/schedule/book")
    public String bookSchedule(@RequestParam("slotId") Long slotId,
                               @RequestParam(name = "scheduleOrderChoice", required = false) Long orderId,
                               RedirectAttributes redirectAttributes) {
        if(orderId == null) {
            redirectAttributes.addFlashAttribute("error_book_order", "Выберете заказ для записи");
            return "redirect:/profile";
        }
        scheduleService.bookSlot(slotId, orderId);
        return "redirect:/profile";
    }

    @PreAuthorize("hasAuthority('ROLE_CLIENT')")
    @PostMapping(path = "/profile/new-car")
    public String addCar(@ModelAttribute("car") CarDto carDto, RedirectAttributes redirectAttributes) {
        if(carService.findByStateNumber(carDto.getStateNumber()).isPresent()) {
            redirectAttributes.addFlashAttribute("error_existing_car",
                    "Машина с таким ГосНомером уже добавлена");
            return "redirect:/profile";
        }
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
