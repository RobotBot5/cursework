package com.rtumirea.KazakovIG.cursework.controllers;

import com.rtumirea.KazakovIG.cursework.domain.dto.ScheduleDto;
import com.rtumirea.KazakovIG.cursework.domain.dto.order.OrderDtoTo;
import com.rtumirea.KazakovIG.cursework.domain.entities.OrderEntity;
import com.rtumirea.KazakovIG.cursework.domain.entities.ScheduleEntity;
import com.rtumirea.KazakovIG.cursework.domain.enums.OrderStatus;
import com.rtumirea.KazakovIG.cursework.mappers.Mapper;
import com.rtumirea.KazakovIG.cursework.services.OrderService;
import com.rtumirea.KazakovIG.cursework.services.ScheduleService;
import com.rtumirea.KazakovIG.cursework.services.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class AutomechOrdersController {

    private OrderService orderService;

    private Mapper<OrderEntity, OrderDtoTo> orderMapperTo;

    private ScheduleService scheduleService;

    private Mapper<ScheduleEntity, ScheduleDto> scheduleMapper;

    private UserService userService;

    public AutomechOrdersController(OrderService orderService,
                                    Mapper<OrderEntity, OrderDtoTo> orderMapperTo,
                                    ScheduleService scheduleService,
                                    Mapper<ScheduleEntity, ScheduleDto> scheduleMapper,
                                    UserService userService) {
        this.orderService = orderService;
        this.orderMapperTo = orderMapperTo;
        this.scheduleService = scheduleService;
        this.scheduleMapper = scheduleMapper;
        this.userService = userService;
    }

    @PreAuthorize("hasAuthority('ROLE_AUTOMECH')")
    @GetMapping(path = "/automech/orders")
    public String getPendingOrders(Model model) {
        List<OrderEntity> automechOrdersEntities = orderService.findByCurrentAutomechAndStatus(OrderStatus.PENDING);
        List<OrderDtoTo> automechOrdersDto = automechOrdersEntities
                .stream().map(orderMapperTo::mapTo)
                .collect(Collectors.toList());
        model.addAttribute("orders", automechOrdersDto);

        List<ScheduleEntity> automechSlotsEntities = scheduleService.getCurrentAutomechSlots();
        List<ScheduleDto> automechSlotsDto = automechSlotsEntities
                .stream().map(scheduleMapper::mapTo)
                .collect(Collectors.toList());

        model.addAttribute("automechSlots", automechSlotsDto);

        return "automech_orders";
    }

    @PreAuthorize("hasAuthority('ROLE_AUTOMECH')")
    @PostMapping(path = "/automech/orders/update-order")
    public String updatePandingOrder(@ModelAttribute OrderDtoTo orderDto, @RequestParam(name = "detailsCheckbox", required = false) Boolean detailsCheckbox) {
        orderDto.setOrderStatus(OrderStatus.AWAITING_SCHEDULING);
        if (detailsCheckbox != null) orderDto.setDetailsWaiting(0);
        OrderEntity orderEntity = orderMapperTo.mapFrom(orderDto);
        orderService.updatePendingStatus(orderEntity);


        return "redirect:/automech/orders";
    }

    @PreAuthorize("hasAuthority('ROLE_AUTOMECH')")
    @PostMapping(path = "/automech/orders/car-ready")
    public String updateCarReady(@RequestParam(name = "carReady") Long orderId) {
        orderService.updateCarStatus(orderId, true);

        return "redirect:/automech/orders";
    }

    @PreAuthorize("hasAuthority('ROLE_AUTOMECH')")
    @PostMapping(path = "/automech/orders/car-unready")
    public String updateCarUnReady(@RequestParam(name = "carReady") Long orderId) {
        orderService.updateCarStatus(orderId, false);

        return "redirect:/automech/orders";
    }

    @PreAuthorize("hasAuthority('ROLE_AUTOMECH')")
    @PostMapping(path = "/automech/orders/order-ready")
    public String updateOrderReady(@RequestParam(name = "orderReady") Long orderId) {
        orderService.updateReadyStatus(orderId);

        return "redirect:/automech/orders";
    }

    @PreAuthorize("hasAuthority('ROLE_AUTOMECH')")
    @PostMapping(path = "/automech/orders/delete-order")
    public String deleteCompletedOrder(@RequestParam(name = "deleteOrder") Long orderId) {
        scheduleService.deleteBookedOrder(orderId);
        userService.decrementOrderNum(orderService.findById(orderId).get().getUserEntity());
        return "redirect:/automech/orders";
    }
}
