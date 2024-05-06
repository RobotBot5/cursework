package com.rtumirea.KazakovIG.cursework.controllers;

import com.rtumirea.KazakovIG.cursework.domain.dto.CarDto;
import com.rtumirea.KazakovIG.cursework.domain.dto.order.OrderDtoFrom;
import com.rtumirea.KazakovIG.cursework.domain.entities.CarEntity;
import com.rtumirea.KazakovIG.cursework.domain.entities.OrderEntity;
import com.rtumirea.KazakovIG.cursework.domain.entities.ServiceEntity;
import com.rtumirea.KazakovIG.cursework.domain.entities.UserEntity;
import com.rtumirea.KazakovIG.cursework.domain.enums.OrderStatus;
import com.rtumirea.KazakovIG.cursework.domain.enums.ServiceType;
import com.rtumirea.KazakovIG.cursework.mappers.Mapper;
import com.rtumirea.KazakovIG.cursework.services.CarService;
import com.rtumirea.KazakovIG.cursework.services.OrderService;
import com.rtumirea.KazakovIG.cursework.services.ServiceService;
import com.rtumirea.KazakovIG.cursework.services.UserService;
import lombok.extern.java.Log;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Log
@Controller
public class OrderController {

    private CarService carService;

    private Mapper<CarEntity, CarDto> carMapper;

    private ServiceService serviceService;

    private Mapper<OrderEntity, OrderDtoFrom> orderMapper;

    private UserService userService;

    private OrderService orderService;

    public OrderController(CarService carService, Mapper<CarEntity, CarDto> carMapper,
                           ServiceService serviceService, Mapper<OrderEntity, OrderDtoFrom> orderMapper,
                           UserService userService, OrderService orderService) {
        this.carService = carService;
        this.carMapper = carMapper;
        this.serviceService = serviceService;
        this.orderMapper = orderMapper;
        this.userService = userService;
        this.orderService = orderService;
    }

    @PreAuthorize("hasAuthority('ROLE_CLIENT')")
    @GetMapping(path = "/profile/orders")
    public String orderList(Model model) {
        model.addAttribute("client_cars", carService
                .findCurrentUserCarsWithoutOrders().stream()
                .map(carMapper::mapTo).collect(Collectors.toList()));
        model.addAttribute("order", new OrderDtoFrom());

        List<ServiceEntity> serviceEntities = serviceService.findAll();
        Map<ServiceType, List<ServiceEntity>> servicesByType = serviceEntities.stream()
                .collect(Collectors.groupingBy(ServiceEntity::getServiceType));
        model.addAttribute("servicesByType", servicesByType);
        Map<String, String> serviceTypeNames = getServiceTypeNames();
        model.addAttribute("serviceTypeNames", serviceTypeNames);
        return "client_orders";
    }

    @PreAuthorize("hasAuthority('ROLE_CLIENT')")
    @PostMapping(path = "/profile/orders/new-order")
    public String createOrder(@ModelAttribute OrderDtoFrom orderDto) {
        OrderEntity orderEntity = orderMapper.mapFrom(orderDto);
        orderEntity.setOrderStatus(OrderStatus.PENDING);
        UserEntity autoMechToOrder = userService.findAutoMechWithMinOrdersNum().get();
        orderEntity.setUserEntity(autoMechToOrder);

        orderService.createOrder(orderEntity, autoMechToOrder);
        return "redirect:/profile/orders";
    }


    public Map<String, String> getServiceTypeNames() {
        Map<String, String> serviceTypeNames = new HashMap<>();
        serviceTypeNames.put("MAINTENANCE", "Техническое обслуживание");
        serviceTypeNames.put("DIAGNOSTICS", "Диагностика");
        serviceTypeNames.put("TRANSMISSION_REPAIR", "Ремонт трансмиссии");
        serviceTypeNames.put("ENGINE_REPAIR", "Ремонт двигателя");
        serviceTypeNames.put("DIESEL_ENGINE_REPAIR", "Ремонт дизельных двигателей");
        serviceTypeNames.put("ELECTRICAL_EQUIPMENT_REPAIR", "Ремонт электрооборудования");
        serviceTypeNames.put("SUSPENSION_REPAIR", "Ремонт ходовой");
        serviceTypeNames.put("STEERING_REPAIR", "Ремонт рулевого управления");
        serviceTypeNames.put("BRAKE_SYSTEM_REPAIR", "Ремонт тормозной системы");
        serviceTypeNames.put("BODY_PAINTING", "Покраска кузова");
        serviceTypeNames.put("DETAILING", "Детейлинг");
        serviceTypeNames.put("BODYWORK_REPAIR", "Кузовной ремонт");
        serviceTypeNames.put("GLASS_REPLACEMENT", "Замена стекол");
        return serviceTypeNames;
    }
}
