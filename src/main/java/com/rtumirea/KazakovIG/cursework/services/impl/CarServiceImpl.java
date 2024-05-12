package com.rtumirea.KazakovIG.cursework.services.impl;

import com.rtumirea.KazakovIG.cursework.domain.entities.CarEntity;
import com.rtumirea.KazakovIG.cursework.domain.entities.UserEntity;
import com.rtumirea.KazakovIG.cursework.repositories.CarRepository;
import com.rtumirea.KazakovIG.cursework.repositories.OrderRepository;
import com.rtumirea.KazakovIG.cursework.services.CarService;
import com.rtumirea.KazakovIG.cursework.services.OrderService;
import com.rtumirea.KazakovIG.cursework.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class CarServiceImpl implements CarService {

    private CarRepository carRepository;

    private UserService userService;

    private OrderRepository orderRepository;

    public CarServiceImpl(CarRepository carRepository, UserService userService, OrderRepository orderRepository) {
        this.carRepository = carRepository;
        this.userService = userService;
        this.orderRepository = orderRepository;
    }

    @Override
    public List<CarEntity> findAll() {
        return StreamSupport.stream(carRepository
                .findAll()
                .spliterator(),
                false)
                .collect(Collectors.toList());
    }

    @Override
    public void createCar(CarEntity carEntity) {
        carRepository.save(carEntity);
    }

    @Override
    public List<CarEntity> findCurrentUserCars() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userPhoneNumber = authentication.getName();
        return carRepository.findAllByUserEntity(
                userService.findByPhoneNumber(userPhoneNumber)
                        .get());
    }

    @Override
    public Optional<CarEntity> findById(Long id) {
        return carRepository.findById(id);
    }

    @Override
    public List<CarEntity> findCurrentUserCarsWithoutOrders() {
        List<CarEntity> currentUserCars = findCurrentUserCars();
        return currentUserCars
                .stream()
                .filter(currentUserCar ->
                        !orderRepository.existsByCarEntity(currentUserCar))
                .collect(Collectors.toList());
    }

    @Override
    public List<CarEntity> findCarsOfClient(UserEntity userEntity) {
        return carRepository.findAllByUserEntity(userEntity);
    }

    @Override
    public void updateCarStatus(CarEntity carEntity, Boolean carReady) {
        carEntity.setCarReady(carReady);
        carRepository.save(carEntity);
    }

    @Override
    public Optional<CarEntity> findByStateNumber(String stateNumber) {
        return carRepository.findByStateNumber(stateNumber);
    }
}
