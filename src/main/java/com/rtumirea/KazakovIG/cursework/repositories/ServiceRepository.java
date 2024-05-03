package com.rtumirea.KazakovIG.cursework.repositories;

import com.rtumirea.KazakovIG.cursework.domain.entities.ServiceEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ServiceRepository extends CrudRepository<ServiceEntity, Long> {
    Optional<ServiceEntity> findByName(String name);
}
