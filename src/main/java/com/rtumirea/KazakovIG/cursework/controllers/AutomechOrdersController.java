package com.rtumirea.KazakovIG.cursework.controllers;

import com.rtumirea.KazakovIG.cursework.domain.dto.order.OrderDtoTo;
import com.rtumirea.KazakovIG.cursework.domain.entities.OrderEntity;
import com.rtumirea.KazakovIG.cursework.domain.enums.OrderStatus;
import com.rtumirea.KazakovIG.cursework.mappers.Mapper;
import com.rtumirea.KazakovIG.cursework.services.OrderService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class AutomechOrdersController {

    private OrderService orderService;

    private Mapper<OrderEntity, OrderDtoTo> orderMapperTo;

    public AutomechOrdersController(OrderService orderService,
                                    Mapper<OrderEntity, OrderDtoTo> orderMapperTo) {
        this.orderService = orderService;
        this.orderMapperTo = orderMapperTo;
    }

    @PreAuthorize("hasAuthority('ROLE_AUTOMECH')")
    @GetMapping(path = "/automech/orders")
    public String getPendingOrders(Model model) {
        List<OrderEntity> automechOrdersEntities = orderService.findByCurrentAutomechAndStatus(OrderStatus.PENDING);
        List<OrderDtoTo> automechOrdersDto = automechOrdersEntities
                .stream().map(orderMapperTo::mapTo)
                .collect(Collectors.toList());
        model.addAttribute("orders", automechOrdersDto);

        return "automech_orders";
    }

    @PreAuthorize("hasAuthority('ROLE_AUTOMECH')")
    @PostMapping(path = "/automech/orders/update-order")
    public String updatePandingOrder(@ModelAttribute OrderDtoTo orderDto) {
        orderDto.setOrderStatus(OrderStatus.IN_PROGRESS);
        OrderEntity orderEntity = orderMapperTo.mapFrom(orderDto);
        orderService.updatePendingStatus(orderEntity);


        return "redirect:/automech/orders";
    }
}
