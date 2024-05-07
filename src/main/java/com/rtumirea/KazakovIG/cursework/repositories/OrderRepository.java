package com.rtumirea.KazakovIG.cursework.repositories;

import com.rtumirea.KazakovIG.cursework.domain.entities.CarEntity;
import com.rtumirea.KazakovIG.cursework.domain.entities.OrderEntity;
import com.rtumirea.KazakovIG.cursework.domain.entities.UserEntity;
import com.rtumirea.KazakovIG.cursework.domain.enums.OrderStatus;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends CrudRepository<OrderEntity, Long> {

    boolean existsByCarEntity(CarEntity carEntity);

    Optional<OrderEntity> findByCarEntity(CarEntity carEntity);

    List<OrderEntity> findAllByUserEntityAndOrderStatus(UserEntity userEntity, OrderStatus status);
}
