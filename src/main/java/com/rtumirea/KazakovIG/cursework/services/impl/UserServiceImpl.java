package com.rtumirea.KazakovIG.cursework.services.impl;

import com.rtumirea.KazakovIG.cursework.domain.entities.UserEntity;
import com.rtumirea.KazakovIG.cursework.repositories.UserRepository;
import com.rtumirea.KazakovIG.cursework.services.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    private PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void addUser(UserEntity userEntity) {
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        userRepository.save(userEntity);
    }

    @Override
    public boolean isUserExists(String phoneNumber) {
        return userRepository.findByPhoneNumber(phoneNumber).isPresent();
    }

    @Override
    public Optional<UserEntity> findByPhoneNumber(String phoneNumber) {
        return userRepository.findByPhoneNumber(phoneNumber);
    }

    @Override
    public Optional<UserEntity> findAutoMechWithMinOrdersNum() {
        return userRepository.findAutoMechsWithMinOrdersNum().stream().findFirst();
    }

    @Override
    public void incrementOrderNum(UserEntity autoMechEntity) {
        autoMechEntity.setOrdersNum(autoMechEntity.getOrdersNum() + 1);
        userRepository.save(autoMechEntity);
    }

    @Override
    public void decrementOrderNum(UserEntity autoMechEntity) {
        autoMechEntity.setOrdersNum(autoMechEntity.getOrdersNum() - 1);
        userRepository.save(autoMechEntity);
    }

    @Override
    public void addAutoMech(String phoneNumber, String name, String password) {
        userRepository.save(UserEntity.builder()
                        .phoneNumber(phoneNumber)
                        .name(name)
                        .password(passwordEncoder.encode(password))
                        .roles("ROLE_AUTOMECH")
                        .ordersNum(0)
                .build());
    }
}
