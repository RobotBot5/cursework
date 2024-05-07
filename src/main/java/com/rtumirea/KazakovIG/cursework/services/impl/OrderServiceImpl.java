package com.rtumirea.KazakovIG.cursework.services.impl;

import com.rtumirea.KazakovIG.cursework.domain.entities.CarEntity;
import com.rtumirea.KazakovIG.cursework.domain.entities.OrderEntity;
import com.rtumirea.KazakovIG.cursework.domain.entities.UserEntity;
import com.rtumirea.KazakovIG.cursework.domain.enums.OrderStatus;
import com.rtumirea.KazakovIG.cursework.repositories.OrderRepository;
import com.rtumirea.KazakovIG.cursework.services.CarService;
import com.rtumirea.KazakovIG.cursework.services.OrderService;
import com.rtumirea.KazakovIG.cursework.services.UserService;
import lombok.extern.java.Log;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    public void createOrder(OrderEntity orderEntity) {
        orderRepository.save(orderEntity);
    }

    @Override
    public boolean isExistCar(CarEntity currentUserCar) {
        return orderRepository.existsByCarEntity(currentUserCar);
    }

    @Override
    public List<OrderEntity> findByCurrentClient() {
        List<CarEntity> carsOfClient = carService.findCurrentUserCars();
        return carsOfClient.stream()
                .map(carEntity -> orderRepository.findByCarEntity(carEntity).orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderEntity> findByCurrentAutomechAndStatus(OrderStatus status) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userPhoneNumber = authentication.getName();
        Optional<UserEntity> userEntity = userService.findByPhoneNumber(userPhoneNumber);
        return orderRepository.findAllByUserEntityAndOrderStatus(userEntity.get(), status);
    }

    @Override
    public void updatePendingStatus(OrderEntity orderEntity) {
        orderRepository.findById(orderEntity.getId()).map(existingOrder -> {
            Optional.ofNullable(orderEntity.getDetailsWaiting()).ifPresent(existingOrder::setDetailsWaiting);
            Optional.ofNullable(orderEntity.getWorkingTime()).ifPresent(existingOrder::setWorkingTime);
            Optional.ofNullable(orderEntity.getOrderStatus()).ifPresent(existingOrder::setOrderStatus);
            return orderRepository.save(existingOrder);
        }).orElseThrow(() -> new RuntimeException("Order does not exist"));
    }
}