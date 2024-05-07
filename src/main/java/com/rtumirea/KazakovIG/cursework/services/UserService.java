package com.rtumirea.KazakovIG.cursework.services;

import com.rtumirea.KazakovIG.cursework.domain.entities.UserEntity;

import java.util.Optional;

public interface UserService {
    void addUser(UserEntity userEntity);

    boolean isUserExists(String phoneNumber);

    Optional<UserEntity> findByPhoneNumber(String phoneNumber);

    void addAutoMech(String phoneNumber, String name, String password);
}
