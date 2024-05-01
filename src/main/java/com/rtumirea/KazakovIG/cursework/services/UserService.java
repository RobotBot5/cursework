package com.rtumirea.KazakovIG.cursework.services;

import com.rtumirea.KazakovIG.cursework.domain.entities.UserEntity;

public interface UserService {
    void addUser(UserEntity userEntity);

    boolean isUserExists(String phoneNumber);
}
