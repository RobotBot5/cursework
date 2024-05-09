package com.rtumirea.KazakovIG.cursework.services;

import com.rtumirea.KazakovIG.cursework.domain.entities.ScheduleEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface ScheduleService {
    void generateSchedule(LocalDate startDate, LocalDate endDate, int intervalInMinutes);

    List<ScheduleEntity> getFreeSlots();
}
