package com.rtumirea.KazakovIG.cursework.mappers.impl.order;

import com.rtumirea.KazakovIG.cursework.domain.dto.order.OrderDtoFrom;
import com.rtumirea.KazakovIG.cursework.domain.entities.OrderEntity;
import com.rtumirea.KazakovIG.cursework.domain.entities.ServiceEntity;
import com.rtumirea.KazakovIG.cursework.mappers.Mapper;
import com.rtumirea.KazakovIG.cursework.services.CarService;
import com.rtumirea.KazakovIG.cursework.services.ServiceService;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class OrderMapperFrom implements Mapper<OrderEntity, OrderDtoFrom> {

    private ServiceService serviceService;

    private CarService carService;

    public OrderMapperFrom(ServiceService serviceService, CarService carService) {
        this.serviceService = serviceService;
        this.carService = carService;
    }

    @Override
    public OrderDtoFrom mapTo(OrderEntity orderEntity) {
        return OrderDtoFrom.builder()
                .id(orderEntity.getId())
                .serviceIds(orderEntity.getServiceEntity().stream().map(ServiceEntity::getId).collect(Collectors.toSet()))
                .carId(orderEntity.getCarEntity().getId())
                .userEntity(orderEntity.getUserEntity())
                .orderStatus(orderEntity.getOrderStatus())
                .detailsWaiting(orderEntity.getDetailsWaiting())
                .workingTime(orderEntity.getWorkingTime())
                .build();
    }

    @Override
    public OrderEntity mapFrom(OrderDtoFrom orderDto) {
        return OrderEntity.builder()
                .id(orderDto.getId())
                .serviceEntity(orderDto.getServiceIds().stream().map(serviceId -> serviceService.findById(serviceId).get()).collect(Collectors.toSet()))
                .carEntity(carService.findById(orderDto.getCarId()).get())
                .userEntity(orderDto.getUserEntity())
                .orderStatus(orderDto.getOrderStatus())
                .detailsWaiting(orderDto.getDetailsWaiting())
                .workingTime(orderDto.getWorkingTime())
                .build();
    }
}
