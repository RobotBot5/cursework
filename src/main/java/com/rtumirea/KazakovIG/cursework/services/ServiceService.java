package com.rtumirea.KazakovIG.cursework.services;

import com.rtumirea.KazakovIG.cursework.domain.entities.ServiceEntity;

import java.util.List;

public interface ServiceService {
    ServiceEntity save(ServiceEntity serviceEntity);

    List<ServiceEntity> findAll();

    boolean isServiceNameExists(String name);

    void delete(String name);

    void updatePriceByName(String name, int price);
}
