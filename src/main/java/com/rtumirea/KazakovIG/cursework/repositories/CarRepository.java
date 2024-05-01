package com.rtumirea.KazakovIG.cursework.repositories;

import com.rtumirea.KazakovIG.cursework.domain.entities.CarEntity;
import com.rtumirea.KazakovIG.cursework.domain.entities.UserEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CarRepository extends CrudRepository<CarEntity, Long> {
    List<CarEntity> findAllByUserEntity(UserEntity userEntity);
}
