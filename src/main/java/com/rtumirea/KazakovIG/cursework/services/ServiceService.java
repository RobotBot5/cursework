package com.rtumirea.KazakovIG.cursework.services;

import com.rtumirea.KazakovIG.cursework.domain.entities.ServiceEntity;

import java.util.List;

public interface ServiceService {
    ServiceEntity save(ServiceEntity serviceEntity);

    List<ServiceEntity> findAll();
}
