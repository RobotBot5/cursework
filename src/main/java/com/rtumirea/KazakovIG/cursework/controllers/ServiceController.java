package com.rtumirea.KazakovIG.cursework.controllers;

import com.rtumirea.KazakovIG.cursework.domain.dto.ServiceDto;
import com.rtumirea.KazakovIG.cursework.domain.entities.ServiceEntity;
import com.rtumirea.KazakovIG.cursework.mappers.Mapper;
import com.rtumirea.KazakovIG.cursework.services.ServiceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class ServiceController {

    private ServiceService serviceService;

    private Mapper<ServiceEntity, ServiceDto> serviceMapper;

    public ServiceController(ServiceService serviceService, Mapper<ServiceEntity, ServiceDto> serviceMapper) {
        this.serviceService = serviceService;
        this.serviceMapper = serviceMapper;
    }

    @PostMapping(path = "/services")
    public ResponseEntity<ServiceDto> createService(@RequestBody ServiceDto serviceDto) {
        ServiceEntity serviceEntity = serviceMapper.mapFrom(serviceDto);
        ServiceEntity savedServiceEntity = serviceService.save(serviceEntity);
        return new ResponseEntity<>(serviceMapper.mapTo(savedServiceEntity), HttpStatus.CREATED);
    }

    @GetMapping(path = "/services")
    public List<ServiceDto> listServices() {
        List<ServiceEntity> serviceEntities = serviceService.findAll();
        return serviceEntities.stream()
                .map(serviceMapper::mapTo)
                .collect(Collectors.toList());
    }
}
