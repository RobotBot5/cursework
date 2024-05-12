package com.rtumirea.KazakovIG.cursework.services;

import com.rtumirea.KazakovIG.cursework.domain.entities.ScheduleEntity;

import java.time.LocalDate;
import java.util.List;

public interface ScheduleService {
    void generateScheduleForMonth(LocalDate startDate, int intervalInMinutes);

    List<ScheduleEntity> getFreeSlots();

    void bookSlot(Long slotId, Long orderId);

    List<ScheduleEntity> getCurrentAutomechSlots();

    void deleteBookedOrder(Long orderId);

    List<ScheduleEntity> getCurrentClientSlots();

    List<ScheduleEntity> getAllSlots();

    void addMonth(int intervalInMinutes);

    void deleteLastMonth();

    boolean existOnlyOneMonth();

    boolean isEmpty();
}
