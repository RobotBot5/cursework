package com.rtumirea.KazakovIG.cursework.repositories;

import com.rtumirea.KazakovIG.cursework.domain.entities.OrderEntity;
import com.rtumirea.KazakovIG.cursework.domain.entities.ScheduleEntity;
import com.rtumirea.KazakovIG.cursework.domain.enums.ScheduleStatus;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ScheduleRepository extends CrudRepository<ScheduleEntity, Long> {

    List<ScheduleEntity> findAllByStatus(ScheduleStatus status);

    Optional<ScheduleEntity> findByOrderEntity(OrderEntity orderEntity);

    Optional<ScheduleEntity> findFirstByOrderByIdDesc();

    @Query("SELECT e FROM ScheduleEntity e WHERE EXTRACT(MONTH FROM e.day) = EXTRACT(MONTH FROM (SELECT MIN(e2.day) FROM ScheduleEntity e2)) AND EXTRACT(YEAR FROM e.day) = EXTRACT(YEAR FROM (SELECT MIN(e3.day) FROM ScheduleEntity e3))")
    List<ScheduleEntity> findAllFirstMonth();

}
