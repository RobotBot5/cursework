package com.rtumirea.KazakovIG.cursework.services;

import com.rtumirea.KazakovIG.cursework.domain.entities.CarEntity;
import com.rtumirea.KazakovIG.cursework.domain.entities.OrderEntity;
import com.rtumirea.KazakovIG.cursework.domain.entities.UserEntity;
import com.rtumirea.KazakovIG.cursework.domain.enums.OrderStatus;
import org.aspectj.weaver.ast.Or;

import java.util.List;
import java.util.Optional;

public interface OrderService {
    void createOrder(OrderEntity orderEntity);

    boolean isExistCar(CarEntity currentUserCar);

    List<OrderEntity> findByCurrentClientAndStatus(OrderStatus status);

    List<OrderEntity> findByCurrentAutomechAndStatus(OrderStatus status);

    void updatePendingStatus(OrderEntity orderEntity);

    void updateCarStatus(Long orderId, Boolean carReady);

    void updateReadyStatus(Long orderId);

    List<OrderEntity> findByCurrentAutomech();

    List<OrderEntity> findByCurrentClient();

    void deleteAutomechById(Long id);

    Optional<OrderEntity> findById(Long orderId);
}
