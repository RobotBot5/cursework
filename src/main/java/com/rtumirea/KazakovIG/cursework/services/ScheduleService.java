package com.rtumirea.KazakovIG.cursework.services;

import com.rtumirea.KazakovIG.cursework.domain.entities.ScheduleEntity;

import java.time.LocalDate;
import java.util.List;

public interface ScheduleService {
    void generateSchedule(LocalDate startDate, LocalDate endDate, int intervalInMinutes);

    List<ScheduleEntity> getFreeSlots();

    void bookSlot(Long slotId, Long orderId);

    List<ScheduleEntity> getCurrentAutomechSlots();

    void deleteBookedOrder(Long orderId);

    List<ScheduleEntity> getCurrentClientSlots();
}
