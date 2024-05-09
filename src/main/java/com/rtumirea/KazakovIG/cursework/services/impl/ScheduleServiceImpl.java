package com.rtumirea.KazakovIG.cursework.services.impl;

import com.rtumirea.KazakovIG.cursework.domain.entities.ScheduleEntity;
import com.rtumirea.KazakovIG.cursework.domain.enums.ScheduleStatus;
import com.rtumirea.KazakovIG.cursework.repositories.ScheduleRepository;
import com.rtumirea.KazakovIG.cursework.services.ScheduleService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ScheduleServiceImpl implements ScheduleService {

    private ScheduleRepository scheduleRepository;

    public ScheduleServiceImpl(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    @Override
    public void generateSchedule(LocalDate startDate, LocalDate endDate, int intervalInMinutes) {
        LocalDate currentDate = startDate;

        LocalTime workStartTime = LocalTime.of(9, 0); // 9:00 утра
        LocalTime workEndTime = LocalTime.of(21, 0); // 9:00 вечера

        LocalTime currentTime = workStartTime;

        while (currentDate.isBefore(endDate.plusDays(1))) {
            if (currentTime.isBefore(workEndTime)) {
                scheduleRepository.save(ScheduleEntity.builder()
                        .day(currentDate)
                        .startTime(currentTime)
                        .endTime(currentTime.plusMinutes(intervalInMinutes))
                        .status(ScheduleStatus.FREE)
                        .build());
                currentTime = currentTime.plusMinutes(intervalInMinutes);
            }
            else {
                currentTime = workStartTime;
                currentDate = currentDate.plusDays(1);
            }
        }
    }

    @Override
    public List<ScheduleEntity> getFreeSlots() {
        List<ScheduleEntity> scheduleEntities = scheduleRepository.findAllByStatus(ScheduleStatus.FREE);
        return scheduleEntities.stream()
                .filter(scheduleEntity ->
                        scheduleEntity.getDay().isAfter(LocalDate.now()))
                .collect(Collectors.toList());
    }
}
