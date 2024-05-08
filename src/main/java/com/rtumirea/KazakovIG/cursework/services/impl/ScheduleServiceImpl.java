package com.rtumirea.KazakovIG.cursework.services.impl;

import com.rtumirea.KazakovIG.cursework.domain.entities.ScheduleEntity;
import com.rtumirea.KazakovIG.cursework.domain.enums.ScheduleStatus;
import com.rtumirea.KazakovIG.cursework.repositories.ScheduleRepository;
import com.rtumirea.KazakovIG.cursework.services.ScheduleService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ScheduleServiceImpl implements ScheduleService {

    private ScheduleRepository scheduleRepository;

    public ScheduleServiceImpl(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    @Override
    public void generateSchedule(LocalDateTime startDate, LocalDateTime endDate, int intervalInMinutes) {
        LocalDateTime currentDateTime = startDate;

        LocalTime workStartTime = LocalTime.of(9, 0); // 9:00 утра
        LocalTime workEndTime = LocalTime.of(21, 0); // 9:00 вечера

        while (currentDateTime.isBefore(endDate)) {
            if ((currentDateTime.toLocalTime().isAfter(workStartTime) || currentDateTime.toLocalTime().equals(workStartTime))
                    && (currentDateTime.toLocalTime().isBefore(workEndTime))) {
                scheduleRepository.save(ScheduleEntity.builder()
                        .startTime(currentDateTime)
                        .endTime(currentDateTime.plusMinutes(intervalInMinutes))
                        .status(ScheduleStatus.FREE)
                        .build());
            }

            currentDateTime = currentDateTime.plusMinutes(intervalInMinutes);
        }
    }
}
