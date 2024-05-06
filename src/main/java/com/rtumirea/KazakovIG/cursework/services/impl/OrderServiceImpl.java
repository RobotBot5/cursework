package com.rtumirea.KazakovIG.cursework.services.impl;

import com.rtumirea.KazakovIG.cursework.domain.entities.CarEntity;
import com.rtumirea.KazakovIG.cursework.domain.entities.OrderEntity;
import com.rtumirea.KazakovIG.cursework.domain.entities.UserEntity;
import com.rtumirea.KazakovIG.cursework.repositories.OrderRepository;
import com.rtumirea.KazakovIG.cursework.services.CarService;
import com.rtumirea.KazakovIG.cursework.services.OrderService;
import com.rtumirea.KazakovIG.cursework.services.UserService;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Log
@Service
public class OrderServiceImpl implements OrderService {

    private OrderRepository orderRepository;

    private UserService userService;

    private CarService carService;

    public OrderServiceImpl(OrderRepository orderRepository, UserService userService, CarService carService) {
        this.orderRepository = orderRepository;
        this.userService = userService;
        this.carService = carService;
    }

    @Override
    public void createOrder(OrderEntity orderEntity, UserEntity autoMechToOrder) {
        userService.incrementOrderNum(autoMechToOrder);
        orderRepository.save(orderEntity);
    }

    @Override
    public boolean isExistCar(CarEntity currentUserCar) {
        return orderRepository.existsByCarEntity(currentUserCar);
    }

    @Override
    public List<OrderEntity> findByCurrentUser() {
        List<CarEntity> carsOfClient = carService.findCurrentUserCars();
        return carsOfClient.stream()
                .map(carEntity -> orderRepository.findByCarEntity(carEntity).orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}
