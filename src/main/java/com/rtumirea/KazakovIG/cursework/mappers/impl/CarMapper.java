package com.rtumirea.KazakovIG.cursework.mappers.impl;

import com.rtumirea.KazakovIG.cursework.domain.dto.CarDto;
import com.rtumirea.KazakovIG.cursework.domain.entities.CarEntity;
import com.rtumirea.KazakovIG.cursework.mappers.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class CarMapper implements Mapper<CarEntity, CarDto> {

    private ModelMapper modelMapper;

    public CarMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public CarDto mapTo(CarEntity carEntity) {
        return modelMapper.map(carEntity, CarDto.class);
    }

    @Override
    public CarEntity mapFrom(CarDto carDto) {
        return modelMapper.map(carDto, CarEntity.class);
    }
}
