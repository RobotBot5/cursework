package com.rtumirea.KazakovIG.cursework.services;

import com.rtumirea.KazakovIG.cursework.domain.entities.CarEntity;
import com.rtumirea.KazakovIG.cursework.domain.entities.OrderEntity;
import com.rtumirea.KazakovIG.cursework.domain.enums.OrderStatus;

import java.util.List;

public interface OrderService {
    void createOrder(OrderEntity orderEntity);

    boolean isExistCar(CarEntity currentUserCar);

    List<OrderEntity> findByCurrentClientAndStatus(OrderStatus status);

    List<OrderEntity> findByCurrentAutomechAndStatus(OrderStatus status);

    void updatePendingStatus(OrderEntity orderEntity);

    void updateCarStatus(Long orderId, Boolean carReady);

    void updateReadyStatus(Long orderId);

    List<OrderEntity> findByCurrentAutomech();
}
