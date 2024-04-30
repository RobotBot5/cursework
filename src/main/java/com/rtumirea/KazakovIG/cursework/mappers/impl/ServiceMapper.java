package com.rtumirea.KazakovIG.cursework.mappers.impl;

import com.rtumirea.KazakovIG.cursework.domain.dto.ServiceDto;
import com.rtumirea.KazakovIG.cursework.domain.entities.ServiceEntity;
import com.rtumirea.KazakovIG.cursework.mappers.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class ServiceMapper implements Mapper<ServiceEntity, ServiceDto> {

    private ModelMapper modelMapper;

    public ServiceMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public ServiceDto mapTo(ServiceEntity serviceEntity) {
        return modelMapper.map(serviceEntity, ServiceDto.class);
    }

    @Override
    public ServiceEntity mapFrom(ServiceDto serviceDto) {
        return modelMapper.map(serviceDto, ServiceEntity.class);
    }
}
