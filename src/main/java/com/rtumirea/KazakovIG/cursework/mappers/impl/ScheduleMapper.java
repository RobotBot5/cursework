package com.rtumirea.KazakovIG.cursework.mappers.impl;

import com.rtumirea.KazakovIG.cursework.domain.dto.ScheduleDto;
import com.rtumirea.KazakovIG.cursework.domain.entities.ScheduleEntity;
import com.rtumirea.KazakovIG.cursework.mappers.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class ScheduleMapper implements Mapper<ScheduleEntity, ScheduleDto> {

    private ModelMapper modelMapper;

    public ScheduleMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public ScheduleDto mapTo(ScheduleEntity scheduleEntity) {
        return modelMapper.map(scheduleEntity, ScheduleDto.class);
    }

    @Override
    public ScheduleEntity mapFrom(ScheduleDto scheduleDto) {
        return modelMapper.map(scheduleDto, ScheduleEntity.class);
    }
}
