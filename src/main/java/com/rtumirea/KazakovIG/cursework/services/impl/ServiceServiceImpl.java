package com.rtumirea.KazakovIG.cursework.services.impl;

import com.rtumirea.KazakovIG.cursework.domain.entities.ServiceEntity;
import com.rtumirea.KazakovIG.cursework.services.ServiceService;
import com.rtumirea.KazakovIG.cursework.repositories.ServiceRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class ServiceServiceImpl implements ServiceService {

    private ServiceRepository serviceRepository;

    public ServiceServiceImpl(ServiceRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
    }

    @Override
    public ServiceEntity save(ServiceEntity serviceEntity) {
        return serviceRepository.save(serviceEntity);
    }

    @Override
    public List<ServiceEntity> findAll() {
        return StreamSupport.stream(serviceRepository
                .findAll()
                .spliterator(),
                false)
                .collect(Collectors.toList());
    }

    @Override
    public boolean isServiceNameExists(String name) {
        return serviceRepository.findByName(name).isPresent();
    }

    @Override
    public void delete(String name) {
        serviceRepository.delete(serviceRepository.findByName(name).get());
    }

    @Override
    public void updatePriceByName(String name, int price) {
         serviceRepository.findByName(name).map(existingService -> {
            existingService.setPrice(price);
            return serviceRepository.save(existingService);
        });
    }

    @Override
    public Optional<ServiceEntity> findById(Long id) {
        return serviceRepository.findById(id);
    }

    @Override
    public void prepareToDelete(String deletableServiceName) {
        ServiceEntity serviceEntity = serviceRepository.findByName(deletableServiceName).get();
        serviceEntity.setDeletable(true);
        serviceRepository.save(serviceEntity);
    }

    @Override
    public void deleteFinally(Long serviceId) {
        serviceRepository.deleteById(serviceId);
    }

    @Override
    public void undelete(Long serviceId) {
        ServiceEntity serviceEntity = serviceRepository.findById(serviceId).get();
        serviceEntity.setDeletable(false);
        serviceRepository.save(serviceEntity);
    }
}
