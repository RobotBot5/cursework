package com.rtumirea.KazakovIG.cursework.services;

import com.rtumirea.KazakovIG.cursework.domain.entities.OrderEntity;
import com.rtumirea.KazakovIG.cursework.domain.entities.UserEntity;

public interface OrderService {
    void createOrder(OrderEntity orderEntity, UserEntity autoMechToOrder);
}
