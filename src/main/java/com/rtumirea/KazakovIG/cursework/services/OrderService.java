package com.rtumirea.KazakovIG.cursework.services;

import com.rtumirea.KazakovIG.cursework.domain.entities.CarEntity;
import com.rtumirea.KazakovIG.cursework.domain.entities.OrderEntity;
import com.rtumirea.KazakovIG.cursework.domain.enums.OrderStatus;

import java.util.List;

public interface OrderService {
    void createOrder(OrderEntity orderEntity);

    boolean isExistCar(CarEntity currentUserCar);

    List<OrderEntity> findByCurrentClient();

    List<OrderEntity> findByCurrentAutomechAndStatus(OrderStatus status);

    void updatePendingStatus(OrderEntity orderEntity);
}
