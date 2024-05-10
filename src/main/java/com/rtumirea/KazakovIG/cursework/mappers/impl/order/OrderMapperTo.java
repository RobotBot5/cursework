package com.rtumirea.KazakovIG.cursework.mappers.impl.order;

import com.rtumirea.KazakovIG.cursework.domain.dto.order.OrderDtoTo;
import com.rtumirea.KazakovIG.cursework.domain.entities.OrderEntity;
import com.rtumirea.KazakovIG.cursework.mappers.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class OrderMapperTo implements Mapper<OrderEntity, OrderDtoTo> {

    private ModelMapper modelMapper;

    public OrderMapperTo(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public OrderDtoTo mapTo(OrderEntity orderEntity) {
        return modelMapper.map(orderEntity, OrderDtoTo.class);
    }

    @Override
    public OrderEntity mapFrom(OrderDtoTo orderDtoTo) {
        return modelMapper.map(orderDtoTo, OrderEntity.class);
    }
}
