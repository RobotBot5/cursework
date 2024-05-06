package com.rtumirea.KazakovIG.cursework.services;

import com.rtumirea.KazakovIG.cursework.domain.entities.CarEntity;
import com.rtumirea.KazakovIG.cursework.domain.entities.OrderEntity;
import com.rtumirea.KazakovIG.cursework.domain.entities.UserEntity;

import java.util.List;

public interface OrderService {
    void createOrder(OrderEntity orderEntity, UserEntity autoMechToOrder);

    boolean isExistCar(CarEntity currentUserCar);

    List<OrderEntity> findByCurrentUser();
}
