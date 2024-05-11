package com.rtumirea.KazakovIG.cursework.controllers;

import com.rtumirea.KazakovIG.cursework.domain.dto.UserDto;
import com.rtumirea.KazakovIG.cursework.domain.entities.OrderEntity;
import com.rtumirea.KazakovIG.cursework.domain.entities.UserEntity;
import com.rtumirea.KazakovIG.cursework.mappers.Mapper;
import com.rtumirea.KazakovIG.cursework.services.OrderService;
import com.rtumirea.KazakovIG.cursework.services.ScheduleService;
import com.rtumirea.KazakovIG.cursework.services.ServiceService;
import com.rtumirea.KazakovIG.cursework.services.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class AdminController {

    private UserService userService;

    private Mapper<UserEntity, UserDto> userMapper;

    private OrderService orderService;

    private ServiceService serviceService;

    private ScheduleService scheduleService;

    public AdminController(UserService userService, Mapper<UserEntity, UserDto> userMapper, OrderService orderService,
                           ServiceService serviceService, ScheduleService scheduleService) {
        this.userService = userService;
        this.userMapper = userMapper;
        this.orderService = orderService;
        this.serviceService = serviceService;
        this.scheduleService = scheduleService;
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

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping(path = "/admin/services/delete-finally")
    public String deleteFinallyService(@RequestParam(name = "finallyDeletableServiceId") Long serviceId,
                                       RedirectAttributes redirectAttributes) {
        List<OrderEntity> allOrders = orderService.findAll();

        if(allOrders.stream()
                .anyMatch(orderEntity -> orderEntity.getServiceEntity().stream()
                        .anyMatch(serviceEntity -> serviceEntity.getId().equals(serviceId))))
        {
            redirectAttributes.addFlashAttribute("error_delete", "Заказы с такой услугой еще есть");
            return "redirect:/admin/services";
        }

        serviceService.deleteFinally(serviceId);

        return "redirect:/admin/services";
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping(path = "/admin/services/undelete-service")
    public String deleteFinallyService(@RequestParam(name = "unDeleteServiceId") Long serviceId) {
        serviceService.undelete(serviceId);

        return "redirect:/admin/services";
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping(path = "/admin/schedule")
    public String getSchedulePage(Model model) {
        model.addAttribute("slots", scheduleService.getAllSlots());

        return "admin_schedule";
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping(path = "/admin/schedule/add-month")
    public String addMonth(@RequestParam(name = "interval") Integer interval) {
        scheduleService.addMonth(interval);
        return "redirect:/admin/schedule";
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping(path = "/admin/schedule/delete-month")
    public String deleteMonth(RedirectAttributes redirectAttributes) {
        if(scheduleService.existOnlyOneMonth()) {
            redirectAttributes.addFlashAttribute("error", "Существует только один месяц!");
            return "redirect:/admin/schedule";
        }

        scheduleService.deleteLastMonth();
        return "redirect:/admin/schedule";
    }
}
