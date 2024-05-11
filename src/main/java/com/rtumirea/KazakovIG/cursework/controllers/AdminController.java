package com.rtumirea.KazakovIG.cursework.controllers;

import com.rtumirea.KazakovIG.cursework.domain.dto.ServiceDto;
import com.rtumirea.KazakovIG.cursework.domain.dto.UserDto;
import com.rtumirea.KazakovIG.cursework.domain.entities.OrderEntity;
import com.rtumirea.KazakovIG.cursework.domain.entities.ServiceEntity;
import com.rtumirea.KazakovIG.cursework.domain.entities.UserEntity;
import com.rtumirea.KazakovIG.cursework.domain.enums.ServiceType;
import com.rtumirea.KazakovIG.cursework.mappers.Mapper;
import com.rtumirea.KazakovIG.cursework.services.OrderService;
import com.rtumirea.KazakovIG.cursework.services.ScheduleService;
import com.rtumirea.KazakovIG.cursework.services.ServiceService;
import com.rtumirea.KazakovIG.cursework.services.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class AdminController {

    private UserService userService;

    private Mapper<UserEntity, UserDto> userMapper;

    private OrderService orderService;

    private ServiceService serviceService;

    private ScheduleService scheduleService;

    private Mapper<ServiceEntity, ServiceDto> serviceMapper;

    public AdminController(UserService userService, Mapper<UserEntity, UserDto> userMapper, OrderService orderService,
                           ServiceService serviceService, ScheduleService scheduleService,
                           Mapper<ServiceEntity, ServiceDto> serviceMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
        this.orderService = orderService;
        this.serviceService = serviceService;
        this.scheduleService = scheduleService;
        this.serviceMapper = serviceMapper;
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

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping(path = "/admin/services")
    public String listServices(Model model) {
        List<ServiceEntity> serviceEntities = serviceService.findAll();
        Map<ServiceType, List<ServiceEntity>> servicesByType = serviceEntities.stream()
                .collect(Collectors.groupingBy(ServiceEntity::getServiceType));
        model.addAttribute("servicesByType", servicesByType);
        Map<String, String> serviceTypeNames = getServiceTypeNames();
        model.addAttribute("serviceTypeNames", serviceTypeNames);
        model.addAttribute("service", new ServiceDto());
        return "admin_services";
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

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping(path = "/admin/services/new-service")
    public String createService(@ModelAttribute("service") ServiceDto serviceDto, RedirectAttributes redirectAttributes) {

        if(serviceService.isServiceNameExists(serviceDto.getName())) {
            redirectAttributes.addFlashAttribute("error_service_name", "Такое название услуги уже существует");
            return "redirect:/admin/services";
        }

        ServiceEntity serviceEntity = serviceMapper.mapFrom(serviceDto);
        serviceService.save(serviceEntity);
        return "redirect:/admin/services";
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping(path = "/admin/services/delete-service")
    public String deleteService(@RequestParam(name = "deletable_service_name", required = false) String deletable_service_name,
                                RedirectAttributes redirectAttributes) {
        if(deletable_service_name == null) {
            redirectAttributes.addFlashAttribute("error_delete_service", "Выберете услугу");
            return "redirect:/admin/services";
        }
        serviceService.prepareToDelete(deletable_service_name);
        return "redirect:/admin/services";
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping(path = "/admin/services/update-cost")
    public String updateCostServiceByName(
            @RequestParam(name = "updatable_service_name", required = false) String updatable_service_name,
            @RequestParam("updatable_service_price") int updatable_service_price,
            RedirectAttributes redirectAttributes) {
        if(updatable_service_name == null) {
            redirectAttributes.addFlashAttribute("error_update_service", "Выберете услугу");
            return "redirect:/admin/services";
        }
        serviceService.updatePriceByName(updatable_service_name, updatable_service_price);
        return "redirect:/admin/services";
    }
}
