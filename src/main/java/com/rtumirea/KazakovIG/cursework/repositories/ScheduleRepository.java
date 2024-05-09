package com.rtumirea.KazakovIG.cursework.repositories;

import com.rtumirea.KazakovIG.cursework.domain.entities.ScheduleEntity;
import com.rtumirea.KazakovIG.cursework.domain.enums.ScheduleStatus;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScheduleRepository extends CrudRepository<ScheduleEntity, Long> {

    List<ScheduleEntity> findAllByStatus(ScheduleStatus status);

}
