package com.rtumirea.KazakovIG.cursework.services;

import java.time.LocalDateTime;

public interface ScheduleService {
    void generateSchedule(LocalDateTime startDate, LocalDateTime endDate, int intervalInMinutes);
}
