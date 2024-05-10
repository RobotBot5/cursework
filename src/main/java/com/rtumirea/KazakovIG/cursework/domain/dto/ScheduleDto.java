package com.rtumirea.KazakovIG.cursework.domain.dto;

import com.rtumirea.KazakovIG.cursework.domain.entities.OrderEntity;
import com.rtumirea.KazakovIG.cursework.domain.enums.ScheduleStatus;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScheduleDto {

    private Long id;

    private LocalDate day;

    private LocalTime startTime;

    private LocalTime endTime;

    private ScheduleStatus status;

    private OrderEntity orderEntity;

}
