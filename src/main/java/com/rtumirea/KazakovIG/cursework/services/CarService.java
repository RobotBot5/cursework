package com.rtumirea.KazakovIG.cursework.services;

import com.rtumirea.KazakovIG.cursework.domain.entities.CarEntity;
import com.rtumirea.KazakovIG.cursework.domain.entities.UserEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public interface CarService {
    List<CarEntity> findAll();

    void createCar(CarEntity carEntity);

    List<CarEntity> findCurrentUserCars();

    Optional<CarEntity> findById(Long id);

    List<CarEntity> findCurrentUserCarsWithoutOrders();

    List<CarEntity> findCarsOfClient(UserEntity userEntity);
}
