package com.rtumirea.KazakovIG.cursework.services;

import com.rtumirea.KazakovIG.cursework.domain.entities.ServiceEntity;

import java.util.List;
import java.util.Optional;

public interface ServiceService {
    ServiceEntity save(ServiceEntity serviceEntity);

    List<ServiceEntity> findAll();

    boolean isServiceNameExists(String name);

    void delete(String name);

    void updatePriceByName(String name, int price);

    Optional<ServiceEntity> findById(Long id);

    void prepareToDelete(String deletableServiceName);

    void deleteFinally(Long serviceId);

    void undelete(Long serviceId);
}
