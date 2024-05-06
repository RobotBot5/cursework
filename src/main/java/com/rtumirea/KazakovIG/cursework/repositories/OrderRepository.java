package com.rtumirea.KazakovIG.cursework.repositories;

import com.rtumirea.KazakovIG.cursework.domain.entities.CarEntity;
import com.rtumirea.KazakovIG.cursework.domain.entities.OrderEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends CrudRepository<OrderEntity, Long> {

    boolean existsByCarEntity(CarEntity carEntity);

}
