package com.rtumirea.KazakovIG.cursework.controllers;

import com.rtumirea.KazakovIG.cursework.domain.dto.ServiceDto;
import com.rtumirea.KazakovIG.cursework.domain.entities.ServiceEntity;
import com.rtumirea.KazakovIG.cursework.domain.enums.ServiceType;
import com.rtumirea.KazakovIG.cursework.mappers.Mapper;
import com.rtumirea.KazakovIG.cursework.services.ServiceService;
import lombok.extern.java.Log;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class AutomechServiceController {

    private ServiceService serviceService;

    private Mapper<ServiceEntity, ServiceDto> serviceMapper;

    public AutomechServiceController(ServiceService serviceService, Mapper<ServiceEntity, ServiceDto> serviceMapper) {
        this.serviceService = serviceService;
        this.serviceMapper = serviceMapper;
    }

    @PreAuthorize("hasAuthority('ROLE_AUTOMECH')")
    @GetMapping(path = "/automech_services")
    public String listServices(Model model) {
        List<ServiceEntity> serviceEntities = serviceService.findAll();
        Map<ServiceType, List<ServiceEntity>> servicesByType = serviceEntities.stream()
                .collect(Collectors.groupingBy(ServiceEntity::getServiceType));
        model.addAttribute("servicesByType", servicesByType);
        Map<String, String> serviceTypeNames = getServiceTypeNames();
        model.addAttribute("serviceTypeNames", serviceTypeNames);
        model.addAttribute("service", new ServiceDto());
        return "automech_services";
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

    @PreAuthorize("hasAuthority('ROLE_AUTOMECH')")
    @PostMapping(path = "/automech_services/new-service")
    public String createService(@ModelAttribute("service") ServiceDto serviceDto, RedirectAttributes redirectAttributes) {

        if(serviceService.isServiceNameExists(serviceDto.getName())) {
            redirectAttributes.addFlashAttribute("error", "Такое название услуги уже существует");
            return "redirect:/automech_services";
        }

        ServiceEntity serviceEntity = serviceMapper.mapFrom(serviceDto);
        serviceService.save(serviceEntity);
        return "redirect:/automech_services";
    }

    @PreAuthorize("hasAuthority('ROLE_AUTOMECH')")
    @PostMapping(path = "/automech_services/delete-service")
    public String deleteService(@RequestParam("deletable_service_name") String deletable_service_name) {
        serviceService.delete(deletable_service_name);
        return "redirect:/automech_services";
    }
}
