package com.rtumirea.KazakovIG.cursework.repositories;

import com.rtumirea.KazakovIG.cursework.domain.entities.UserEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends CrudRepository<UserEntity, Long> {

    Optional<UserEntity> findByPhoneNumber(String phoneNumber);

    @Query("SELECT u FROM UserEntity u WHERE u.roles = 'ROLE_AUTOMECH' AND u.ordersNum = (SELECT MIN(u2.ordersNum) FROM UserEntity u2 WHERE u2.roles = 'ROLE_AUTOMECH')")
    List<UserEntity> findAutoMechsWithMinOrdersNum();
}
