package com.rtumirea.KazakovIG.cursework.services;

import com.rtumirea.KazakovIG.cursework.domain.entities.CarEntity;

import java.util.List;

public interface CarService {
    List<CarEntity> findAll();

    void createCar(CarEntity carEntity);

    List<CarEntity> findCurrentUserCars();
}
